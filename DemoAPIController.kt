/**
 * A controller to interact with the API for fetching drink data
 */
class APIController (var context: Context) {

/**
 * Fetches a list of drinks by drink type (beer or wine) from the API.
 * @param drink: The type of drink, "beer" or "wine"
 * */
   fun getAllByDrinkType(drink: String){
       // The API URL for retrieving drink details by type.
       var url = ""
       if (drink.equals("beer", ignoreCase = true)) {
           url = ""
       } else if (drink.equals("wine", ignoreCase = true)) {
           url = ""
       }
        // Create a new request using Volley
       val queue: RequestQueue = Volley.newRequestQueue(context)
        // A JSON string request to fetch the data
       val request = StringRequest(
           Request.Method.POST,
           url,
           { response ->
               try {
                   // Parse the response into a List of Drink objects using Gson
                   val jArray = JSONArray(response)
                   val DrinkType = object : TypeToken<List<Drink>>() {}.type
                   val drinks: List<Drink> = Gson().fromJson(jArray.toString(), DrinkType)

                   // Start SubMenuActivity and pass the fetched data
                   val intent = Intent(context, SubMenuScreen::class.java)
                   intent.putParcelableArrayListExtra("drinkList", ArrayList(drinks))
                   context.startActivity(intent)

                   // LogCat message for response
                   Log.e("API Response", response.toString())
                   // LogCat message for parsed data
                   Log.d("Parsed Data", drinks.toString())
               } catch (e: JSONException) {
                   e.printStackTrace()
                   // Handle JSON parsing error
                   Toast.makeText(context, "Error parsing JSON", Toast.LENGTH_LONG).show()
               }
           },
           { error ->
               Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
           })
       queue.add(request)
   }

/**
 * Retrieves drink details by drink ID from the API
 *
 * @param drink - The ID of the drink
 * @param onDrinkNameReceived - Callback function invoked for drink name and image URL.
 * */
    fun getAllDrinksById(drink: Int, onDrinkNameReceived: (String, String?) -> Unit) {
        //The API provides an Array with Objects.
        val drinkIdURL = ""
        val queue: RequestQueue = Volley.newRequestQueue(context)

        // Adding a key-value pair to the JSON Object.
        val jsonObject = JSONObject()
        jsonObject.put("drinkId", drink)
        // A JSON array request to fetch the data
        val request = JsonArrayRequest(
           Request.Method.POST,
           drinkIdURL,
           null,
            { response ->
                try {
                    // Parse the JSON response into a list of Drink objects using Gson
                    val drinks: List<Drink> = Gson().fromJson(
                        response.toString(),
                        object : TypeToken<List<Drink>>() {}.type
                    )
                    // Find the specific drink by matching the provided drink ID
                    val targetDrink = drinks.find { it.id == drink }

                    // If the target drink is found, extract name and image URL
                    if(targetDrink != null){
                        val imageUrl = targetDrink.image
                        targetDrink.name?.let { onDrinkNameReceived(it, imageUrl) }
                    }
                    // LogCat messages for API response and parsed data
                    Log.e("API Response", response.toString())
                    Log.d("Parsed Data", drinks.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Show a toast message if there's an error parsing JSON
                    Toast.makeText(context, "Error parsing JSON", Toast.LENGTH_LONG).show()
                }
            },
            // Error message
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        // Add the request to the RequestQueue to initiate the API call
        queue.add(request)
        }

    fun getDataScan(id: String, callback: (Drink?) -> Unit) {
        val url = ""
        // Create a new request queue
        val queue: RequestQueue = Volley.newRequestQueue(context)
        // Used to get JSON data
        val gson = Gson()

        // Make an API request to fetch the data
        val request = StringRequest(
            Request.Method.POST,
            url,
            { response ->
                try {
                    Log.d("Server Response", response)
                    // Parse the JSON response, allows you to work with the data
                    val jsonObject = JSONObject(response)
                    // Get the data object from the JSON response
                    val dataObject = jsonObject.getJSONObject("data")
                    // Get the drink object from the data object
                    val drinkArray = dataObject.getJSONArray("drink")
                    // Parse the first drink object from the array
                    val drink = gson.fromJson(drinkArray.get(0).toString(), Drink::class.java)

                    Log.d("Parsed Drink", drink?.toString() ?: "null")

                    callback(drink) // Invoke the callback with the drink data
                } catch (e: Exception) {
                    Log.e("Exception", e.message, e)
                    e.printStackTrace()
                    Toast.makeText(context, "Fout bij het verwerken van de gegevens", Toast.LENGTH_LONG).show()
                    callback(null) // Invoke the callback with null if an error occurred
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(context, "Geen respons", Toast.LENGTH_LONG).show()
                callback(null) // Invoke the callback with null if there was no response
            })
        queue.add(request)
    }
}








