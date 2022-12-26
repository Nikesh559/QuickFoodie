# QuickFoodie
Built fully functional Java Backend for food ordering app using Spring Boot, MongoDB and Redis 

# Technologies used
* Java
* Spring Boot
* Spring Security
* MongoDB
* Redis
* Jackson

# Features
 - Performed Searching of nearest restaurants and delivery executives using Geospatial data from MongoDB.
 - Implemented API caching mechanism using Redis.
 - Performed user authentication and session management using Spring Security and JWT. 


###  Customer Service
Performs customer registration, authentication and stores bookings information of customers. 
Method	| Path	| Description	| Role |
------------- | ------------------------- | ------------- | ---------
POST | /customer/register | Register Customer | USER |
POST | /customer/login | Login into account | USER |
GET	| /customer/{custId}	| Get Customer Details | USER | 
GET	| /customer/bookings	| Get all bookings of customer | USER | 
GET	| /product/booking/{bookingId}	| Get booking by bookingId	| USER | 
GET | /logout | Logout of account | USER/ADMIN | 
 
### Booking Service
Used for booking cars and cancellation, maintaining records of cars in stock and its history. 

Method	| Path	| Description	| ROLE
------------- | ------------------------- | ------------- | ------------
GET | /explore/cars | Get all cars records | USER
GET | /explore/car/{carId} | Get car record by id | USER
POST	| /book	| Book a car | USER
DELETE | booking/{bookingId} | Delete booking | USER
GET	| /logs/cars/{carId}	| Get Car history	 | ADMIN
GET	| /logs/customer/{customer}	| Get Customer booking history	| ADMIN







