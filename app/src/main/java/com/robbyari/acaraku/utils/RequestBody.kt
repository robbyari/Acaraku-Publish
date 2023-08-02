package com.robbyari.acaraku.utils


fun requestBody(idOrder: String, totalPrice: String, idEvent: String, nameEvent: String, firstName: String, email: String): Map<String, Any> {
    val params = HashMap<String, Any>()

    val transactionDetails = HashMap<String, String>()
    transactionDetails["order_id"] = idOrder
    transactionDetails["gross_amount"] = totalPrice

    val creditCard = HashMap<String, String>()
    creditCard["secure"] = "true"

    val itemDetails = ArrayList<Map<String, Any>>()
    val item = HashMap<String, Any>()
    item["id"] = idEvent
    item["price"] = totalPrice
    item["quantity"] = 1
    item["name"] = nameEvent
    itemDetails.add(item)

    val customerDetails = HashMap<String, String>()
    customerDetails["first_name"] = firstName
    customerDetails["email"] = email

    params["transaction_details"] = transactionDetails
    params["credit_card"] = creditCard
    params["item_details"] = itemDetails
    params["customer_details"] = customerDetails

    return params
}