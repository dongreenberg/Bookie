#Bookie
Timothy Boehm, Don Greenberg, Steven Rothstein

A web+iOS+Android suite allowing Binghamton University students to manage extracurricular events and organizations, as well as import their academic calendars to their local calendar of choice.

#Platforms, Setup, and Requirements- 

Bookie runs on a number of platforms:
1. Wamp -
  WampServer Version 2.4
  http://www.wampserver.com
  WampServer must be run on a Windows machine!
2. MySql -
	Version 5.6.12
3. Apache -
	Version 2.4.4
4. PHP -
	Version 5.4.12
5. XCode
6. Android Development Tools (ADT) -
	http://developer.android.com/index.html
7. Google Nexus 7 (For testing) -
	Any Android device running an OS >= Android 4.0 will be supported
8. Apple iPhone 5 -
	Any iPhone running an OS >= iOS 5 will be supported

#Database Setup

The database tables necessary to run the program can be created and configured via the following steps -

In phpMyAdmin, create a new MySQL user called "Tommy", with password "pickles" and all read/write permissions.
Next, select "Import" at the top center of the screen. Import the file "model.sql" which has been included with
our source submission.

If successful, you should see 8 tables in the database.

#iOS Setup

Import the project into Xcode

#Android Setup

Import the Bookie project along with the google-play-services_lib project into Android Development Tools Eclipse.

To setup Push notifications for Android, follow the tutorial found at http://developer.android.com/google/gcm/gs.html
Note the Project Number. Change the variable "SENDER_ID" in MainActivity.java to this Project Number.
When creating the API Key, change the variable "$apiKey" in sendAndroidNotification.php to this value.

Make sure Google Play Services are setup according to Step 1 on the following page:
http://developer.android.com/google/gcm/client.html

--NOTE Either register for a DNS name, or change the global HTTPUtility.java and IP variables in the iOS and Android code to your server's IP.

#PHP Setup -

Copy all of the PHP and HTML source into the base directory of the server. Make sure this directory is visible to outside IP's.

#Running the Project and Using Bookie

The website we created is for organization and event creation. The mobile app is for users to control their subscriptions
and events, as well as importing a class schedule. The mobile app uses the data created on the website.

1. Install the .apk of Bookie onto your device. Within ADT Eclipse, this installation can be done by opening any Activity class
and hitting run (assuming your device is connected and USB Debugging is enabled).

2. When the app opens, click "Create a Bookie Account" and enter your credentials.

3. Assuming the HTML and PHP pages on your webserver are visible, direct your browser to http://YOUR_IP/login.html

4. Log in using the credentials you just created from the app. You should now be on the home.html page

5. Click "Create Organization" to create your own organization (it will be added to the database).

6. On home.html, click "Create Event" to create an event for any organization in which you are an admin.
By default, you are an admin for any organization that you create. Note that the start month and end month
are on a scale starting from 0. In other words, 0 is January, 1 is February, ..., 11 is December.
Upon hitting submit, the event will be added to all Android device calendars for logged in users that are
subscribed to the organization for which the event was created.

7. On home.html, click "Add Admin" and input a Bookie username which you would like to add as an admin for
any organization in which you yourself are an admin. This privilege will allow that user to create events
for that organization.

8. On home.html, click "Logout" to end your session.

In the main screen of the Android App:

9. Click "My Organizations" to view your subscribed organizations.
Click an organization from the list to see its events and optionally unsubscribe.
Clicking an event on the list will allow you to add the event directly to your calendar.

10. Click "Browse Organizations" to view all organizations to which you are not
subscribed. The functionality of this page is exactly the same as in Step 9, except that
clicking an organization from the list allows you to subscribe (not unsubscribe).

11. Click "My Events" to view the events which you have added to your calendar, or were
added to your calendar automatically through your subscriptions.

12. Click "Import My Schedule" to import your Binghamton class schedule into your phone calendar.
Choose the term desired from the dropdown menu and enter your BUBrain credentials.

13. On any page, click the 3 squares in the top right corner (on the Action Bar) to view Settings
or log out.

14. The Settings allow you to choose a calendar on your device, allow notifications, allow sync,
and use reminders with a specified reminder time (15 minutes, 20 minutes, etc).
