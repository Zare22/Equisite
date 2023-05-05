package hr.sonsanddaughters.equisite.framework

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import hr.sonsanddaughters.equisite.model.User
import hr.sonsanddaughters.equisite.util.FirebaseUtil


fun FirebaseAuth.registerUser(context: Context, user: User, password: String) =
    createUserWithEmailAndPassword(user.email, password).addOnCompleteListener {
        if (!it.isSuccessful) { Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show() }
        else {
            user.uid = it.result.user?.uid.toString()
            addUserToCollection(context, user)
        }
    }

private fun addUserToCollection(context: Context, user: User) {
    FirebaseUtil.db.collection("users").whereEqualTo("email", user.email).get()
        .addOnCompleteListener { querySnapshotTask ->
            if (!querySnapshotTask.result.isEmpty) { Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show() }
            else {
                FirebaseUtil.db.collection("users").add(user).addOnCompleteListener {
                    if (it.isSuccessful) { Toast.makeText(context, "User registered", Toast.LENGTH_LONG).show() }
                    else { Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show() }
                }
            }
        }
}

fun FirebaseFirestore.updateBalance(uid: String) {
    getIncomes(uid).addOnSuccessListener { incomes ->
        getExpenses(uid).addOnSuccessListener { expenses ->
            val sum = incomes - expenses
            setUserCurrentBalance(uid, sum)
        }
    }
}

private fun FirebaseFirestore.setUserCurrentBalance(uid: String, sum: Double) {
    this.collection("users")
        .whereEqualTo("uid", uid)
        .get()
        .addOnSuccessListener { snapshot ->
            if (!snapshot.isEmpty) {
                val document = snapshot.documents[0]
                document.reference.update("currentBalance", sum)
            }
        }
}

fun FirebaseFirestore.getUserBalance(uid: String, listener: (Double) -> Unit, onFailure: ((Exception) -> Unit)? = null): ListenerRegistration {
    val userRef = collection("users").whereEqualTo("uid", uid)
    return userRef.addSnapshotListener { snapshot, error ->
        if (error != null) { onFailure?.invoke(error) }
        val balance = if (snapshot != null && !snapshot.isEmpty) {
            val document = snapshot.documents[0]
            document.getDouble("currentBalance") ?: 0.0
        } else { 0.0 }

        listener(balance)
    }
}

fun FirebaseFirestore.getExpenses(uid: String): Task<Double> {
    val expensesRef = collection("expenses").whereEqualTo("uid", uid)
    return expensesRef.get().continueWith { task ->
        val documents = task.result
        var totalExpenses = 0.0
        for (document in documents) {
            totalExpenses += document.getDouble("amount") ?: 0.0
        }
        totalExpenses
    }
}

fun FirebaseFirestore.getIncomes(uid: String): Task<Double> {
    val expensesRef = collection("incomes").whereEqualTo("uid", uid)
    return expensesRef.get().continueWith { task ->
        val documents = task.result
        var totalIncomes = 0.0
        for (document in documents) {
            totalIncomes += document.getDouble("amount") ?: 0.0
        }
        totalIncomes
    }
}
