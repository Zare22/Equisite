package hr.sonsanddaughters.equisite.model.transaction

import com.google.firebase.firestore.PropertyName
import hr.sonsanddaughters.equisite.enum.IncomeType
import hr.sonsanddaughters.equisite.model.User
import java.util.*

class Income(transactionName: String, amount: Double, date: Date, description: String, uid: String,@PropertyName("incomeType") val incomeType: String) :
    Transaction(transactionName, amount, date, description, uid) {
    override fun getType(): String = "Income"
}