# HokieHelper
CS 3714 Group Project

**Project Proposal:**

We plan on creating an application that allows Virginia Tech students to enter their PID & password into our app, which will then allow our app to access the students class information. The class information will be able to automatically organize the student’s schedule, and notify the student of any exam time conflicts (3 exams within 24 hours or 2 during the same time).

The app will also notify students when they should leave for class. Our app will use GPS coordinates, and determine the distance the student is from their classroom. Using an average walking speed (~2.1 mph) with a 3-5 minute buffer, if the student is on campus, the app will notify the student an approximate time they need to leave in order to get to class on time. Furthermore, while on campus, the app will provide walking directions on how to get to the hall where that class is held. If the student is off campus, the app will notify the student 30 minutes prior to class that they need to get to campus, but we will not be providing bus schedule information.

The app will push notifications to the pebble, so the student will not have to look at their phone the whole time. Also, the directions (while on campus) will be pushed to the pebble.

Design for: Android / pebble 
Sensors to use: GPS, bluetooth, accelerometer, (web crawler?)

**Client:**
- Name: Sumit Kumar (one of CPEs)	
- Email: timus@vt.edu

**List of Deliverables:**
- Android application communicating with VT servers to access student information
- Notifications reminding students when to leave for class
- Directions leading student to class location
- Determine whether student has exam time conflicts
- Notifications and directions displayed on the pebble watch
