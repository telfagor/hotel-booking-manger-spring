This project is an MVP product that aims to test the European market for the provision of apartment rental services for a short 
period of time. The functionalities listed below are the starting ones. It is possible that during the development of 
the MVP product, their list will be updated.

There are 4 basic entities in the project:
- **User**: Represents the customers and administrators of the system. The main entity.
- **UserDetail**: Stores additional information about users, such as phone number, birthdate, and profile photo.
- **Apartment**: Represents the available apartments with their characteristics (e.g., number of rooms, seats, type, and cost).
- **Order**: Manages booking requests from users for specific apartments, including check-in/check-out dates and booking status.

Actions in the system.
Add actions:
- Adding a new simple user to the system.
- Adding a new apartment to the system.
- Adding a new order where the apartment, the user and the period of stay are indicated.
- Adding additional information about the user if it is missing when creating the order.

Viewing actions:
- View list of all apartments (client & admin)
- View a list of all your orders by filter (client & admin)
- View list of all orders by filter (admin)

Application actions:
- The client forms an application indicating all parameters
- The administrator reviews the application and can either approve or reject it.
- The client can download his order.

Universal functionalities:
- Paging and filtering functionality is provided
- Several languages are supported
- Authentication is done through the email and password
- Authorization functionality is provided, some pages can only be accessed by the administrator