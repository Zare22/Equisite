package hr.sonsanddaughters.equisite.framework

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import hr.sonsanddaughters.equisite.model.User
import hr.sonsanddaughters.equisite.util.FirebaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine


suspend fun FirebaseAuth.registerUser(context: Context, user: User, password: String) =
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
