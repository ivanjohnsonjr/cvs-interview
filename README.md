# cvs-interview
Consume Rick and Morty character api

### Description <br/>
This app will display Rick and Morty character for the query entered in the search box.
This app will allow the user to see the detail information of the character the user click on from the 
search results screen.


### Structure<br/>

***Core:*** <br/>
    - repo<br/>
        - RickMortyApi - api interface<br/>
        - RickMortyPagingSource - Paging Source used for pulling the chunks of data from backend<br/>
        - RickMortyRepository - Repository model encapsulating the data<br/>
    - CoreConstants - constant data used in the core <br/>
    - CoreDi - Hilt dependency inject class<br/>
****UI:**** <br/>
    - detail/CharacterDetailScreen - Compose screen for showing Character details<br/>
    - home/HomeScreen - The main home screen use for displaying all the characters<br/>
    - nav/MainNavHost - Navigation Host used for managing navigation between screens<br/>
    - vm/ShareViewModel - ViewModel used for storing HomeScreen and CharacterDetailScreen information<br/>
    - UIConstants - Constants for the ui <br/>

****MainActivity**** - The Single Activity used for displaying the screen<br/>
****RickMortyApplication**** - Lightweight Application instance used for initializing Hilt<br/>

