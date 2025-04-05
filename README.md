# cvs-interview
Consume Rick and Morty character api

Description: This app will display Rick and Morty character for the query entered in the search box.
This app will allow the user to see the detail information of the character the user click on from the 
search results screen.


Structure

Core: 
    - repo
        - RickMortyApi - api interface
        - RickMortyPagingSource - Paging Source used for pulling the chunks of data from backend
        - RickMortyRepository - Repository model encapsulating the data
    - CoreConstants - constant data used in the core 
    - CoreDi - Hilt dependency inject class
ui:
    - detail/CharacterDetailScreen - Compose screen for showing Character details
    - home/HomeScreen - The main home screen use for displaying all the characters
    - nav/MainNavHost - Navigation Host used for managing navigation between screens
    - vm/ShareViewModel - ViewModel used for storing HomeScreen and CharacterDetailScreen information
    - UIConstants - Constants for the ui 

MainActivity - The Single Activity used for displaying the screen
RickMortyApplication - Lightweight Application instance used for initializing Hilt

