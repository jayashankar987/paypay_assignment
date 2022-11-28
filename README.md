# Assignment for PayPay

## Requirement:

  ### Exchange Current App with provided backend api
  * Input field which accepts decimal valu
  * Dropdown to select the current for the input decimal
  * List to display all the converted list for the input to vairious supported currencies inside API
  
  ### Important Cases:
  * App should be using free account
  * To avoid using more bandwidth, app should connect to network only after 30 mins for fetching latest currency rates
  * Best Architecture and code pratices
  * Unit testcases to test the quality of code
  * Need to support offline in case on no network
  
# Technical Details
  * ## Architecture: 
      - MVVM, Clean Architecture
  * ## Libraries: 
      - Kotlin, Coroutines, Kotlin Flows, Retrofit, Room, Moshi, Hilt, MockK, JUnit4

# Modules
  * `:data`: 
      This Module is a Kotlin Libary modules which has network layer implementation to be able to download data and provide accordingly using API Service.
 * `:framework`:
      This module has a Android Library project which deals with Models, Entities, DB, Usecase and Repository layer to have information to be sent to requested viewModel.
 * `:app`:
      This module has the UI and ViewModel needed to interact with `:framework` module and UI for the end user.

# Installation Guide:
Import Project in Android Studio and make sure SDK, Kotlin, JDK are available ready for setup and run `app`

# Using Terminal: 

  * `./gradlew clean installDebug` for Mac
  * `.gradlew clean installDebug` for Windows
   
 ```
 Note: make sure the required setup is done and inside terminal 
       for gradle and go to root folder of the project and 
       run below commands inside terminal
 ```

# App Preview: 
https://user-images.githubusercontent.com/1208007/203559689-f055a845-f4b3-44fe-9685-9569d47d5748.mp4



