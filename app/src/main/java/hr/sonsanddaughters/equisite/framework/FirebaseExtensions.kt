package hr.sonsanddaughters.equisite.framework

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hr.sonsanddaughters.equisite.model.User
import hr.sonsanddaughters.equisite.util.FirebaseUtil


fun FirebaseAuth.registerUser(context: Context, user: User, password: String) =
    createUserWithEmailAndPassword(user.email, password).addOnCompleteListener {
        if (!it.isSuccessful) {
            Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
        } else {
            user.uid = it.result.user?.uid.toString()
            addUserToCollection(context, user)
        }
    }

private fun addUserToCollection(context: Context, user: User) {
    FirebaseUtil.db.collection("users").whereEqualTo("email", user.email).get()
        .addOnCompleteListener { querySnapshotTask ->
            if (!querySnapshotTask.result.isEmpty) {
                Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show()
            } else {
                FirebaseUtil.db.collection("users").add(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "User registered", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
}
