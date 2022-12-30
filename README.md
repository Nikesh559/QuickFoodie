# QuickFoodie
Built fully functional Java Backend for food ordering app using Spring Boot, MongoDB and Redis 

# Technologies used
* Java
* Spring Boot
* Spring Data
* Spring Security
* MongoDB
* Redis
* Jackson

# Features
 - Implemented API caching mechanism using Redis.
 - Used Spring Data to access MongoDB.  
 - Used Geospatial data to find nearest restaurants & delivery executives.
 - Performed user authentication and session management using Spring Security and JWT. 

# REST APIs
* User Sign Up, Login & Logout

Method	| Path	| Description	
------------- | ------------------------- | ------------- 
POST | /user/register | User Sign Up 
POST | /user/login | Sign In
POST	| /user/logout	| Sign Out 

* Search nearby Restaurants and dishes

Method	| Path	| Description	
------------- | ------------------------- | ------------- 
GET | /search/restaurants | Fetch all restaurants in 5 km radius from user location   
GET	| /search/{dishName}	| Get dishes from restaurants available in 5km radius. 	 
GET	| /search/restaurant/{restaurant}/menus	| Get menus of a restaurant

* Add items to cart

Method	| Path	| Description	
------------- | ------------------------- | ------------- 
POST	| /cart/item/{dishId,qty}	| Add item to cart
GET	| /cart/items	| Get all cart items
GET	| /cart/item-total	| Get grand total of items in cart

* Place order 

Method	| Path	| Description	
------------- | ------------------------- | ------------- 
POST	| /order/cart-items	| Place order 
GET	| /order/{orderId}	| Get Order details





