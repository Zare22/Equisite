package hr.sonsanddaughters.equisite.model

import com.google.firebase.database.PropertyName

data class User(
    @PropertyName("uid") var uid: String? = null,
    @PropertyName("firstName") val firstName: String,
    @PropertyName("lastName") val lastName: String,
    @PropertyName("userName") val userName: String,
    @PropertyName("email") val email: String
    ) {
}