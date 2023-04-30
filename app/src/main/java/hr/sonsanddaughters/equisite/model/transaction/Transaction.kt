package hr.sonsanddaughters.equisite.model.transaction

import com.google.firebase.firestore.PropertyName
import hr.sonsanddaughters.equisite.enum.IncomeType
import hr.sonsanddaughters.equisite.model.User
import java.util.*

abstract class Transaction(
    @PropertyName("transactionName") val transactionName: String,
    @PropertyName("amount") val amount: Double,
    @PropertyName("date")  val date: Date,
    @PropertyName("description")  val description: String,
    @PropertyName("uid") val uid: String
) {
    abstract fun getType(): String
}
