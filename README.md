# HambaApp
Tourism Application

Main Activity:

Our main activity acts like the welcome page with two prominent buttons, one for logging in and the other for signing up.

Register & Login:

When you sign up or log in, we rely on authentication. Essentially, it's all about your email and password. You'll use these credentials to access your account. Your Full Name and Phone Number are securely stored in our Realtime database under your user profile (located around Lines 44-49). Once saved, a message will pop up confirming the successful storage.

Forgot Password:

Regarding the "Forgot Password" feature, there isn't any code in the Forgot Password Layout; it's purely for visual design. However, the code responsible for this function is embedded in the Login Activity, see pages 53 to 99.

Bottom Navigation:

Our app has a bottom menu that appears on most screens. We manage its functionality through the "navigationBar()" method. However, it's essential to note that the Location, Favorites, and Profile sections are still under construction, so they won't take you anywhere just yet.

More Info and Options:

These sections serve as repositories for additional information and various options.

BusinessList, BusinessDescription, and BusinessInfo:

All the business data is stored in our Realtime database.  I have changed the way in which data will be saved. As you will see on the database, the USERID from the authentication is visible on the real-time database and is used, information of an authenticated user will fall under the USERID, There are a few issues, so I am still working on making sure that it functions properly. Retrieval of data did work but now it's not working, so data retrieval has been needs to be worked on.

Given the complexities encountered, I've decided to divide the tasks among specific individuals:

Lilitha Marubs: Your responsibility is to implement validation checks for the text input fields, ensuring that users provide accurate information. While email format validation has already been handled, you can focus on the remaining fields.

Juliet: Your role is to help me with retrieving data from the database and presenting it in an organized manner.

Tebza: You can do the map, integrating the Maps API into our app.

Lisa: tbc

To view the information on the data, you need to use the CTRLALTELITE details. After changing the database, signing up doesn't work. I have been trying to figure out why.


For now use this user:
email: control@gmail.com
Password: Password1234





