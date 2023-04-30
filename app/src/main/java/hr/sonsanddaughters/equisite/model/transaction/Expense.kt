package hr.sonsanddaughters.equisite.model.transaction

import com.google.firebase.firestore.PropertyName
import hr.sonsanddaughters.equisite.enum.ExpenseType
import hr.sonsanddaughters.equisite.model.User
import java.util.*

class Expense(transactionName: String, amount: Double, date: Date, description: String, uid: String,@PropertyName("expenseType") val expenseType: String) :
    Transaction(transactionName, amount, date, description, uid) {
    override fun getType(): String = "Expense"
}