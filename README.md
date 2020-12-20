**The software demo video is in the pic&video folder.**

**The server will be running until 2021/04/22.**

**The server was deployed in Shanghai.**

Technology used in the game-server：

- Spring MVC
- Mysql
- Mybatis
- Maven
- Android

The algorithm server needs to run serverUI.py to start.

The email, user name and password of the test account are all 000.

![register](/pics&video/1.png)

One email account can only register one account. When the mailbox has been registered, it will prompt "registration failed".
After successful registration, it will jump to the login page.

![register](/pics&video/2.png)

When the user logs in, if the password is wrong or the user does not exist, the corresponding prompt will be given.
In addition, there is the function to remember the user name and password.

![game mode](/pics&video/3.png)



The four buttons from top to bottom are "game breakthrough mode", "game practice mode", "history graffiti" and "Game Description".

![game description](/pics&video/4.png)

Players can check the game instructions to learn how to play the game.

Description: 

Computer random questions, players in 20 seconds to complete painting. If the computer guesses that the player's drawing is consistent with the title, it will pass.
There are two game modes:
Breakthrough mode: players can get corresponding points after completing a level, and the points can be accumulated.
Practice mode: players practice freely without setting points.

![game description](/pics&video/5.png)

In the History Doodle Record, players can view a record of their past paintings.

![game description](/pics&video/6.png)

Brush thickness function: provide 4px, 8px, 15px three thickness of the brush to choose from, through a pop-up window for the user to choose.
Color selection function: Provide black, red, yellow and blue colors for user to choose through pop-up window.
Eraser function: set the eraser size according to the brush thickness, change the color pixel to the background color of white, users can erase the content freely.
Clear function: quickly clear all the pixels on the canvas.
Undo: undo the previous operation, you can go back to the first step.
Redo function: Redo the previous operation, you can go back to the last step.

![game description](/pics&video/7.png)

Game practice mode
Return function: pop up a custom dialog, according to the user's choice to return to the menu or continue the game.

Hint function: Draw different hint diagrams for different topics by mapping two objects one by one.

Start Game: Click on the Start Game button to hide the button and show the Submit button. Click the submit button to invoke server save interface to upload pictures to the server, and then invoke recognition interface to recognize the contents of the pictures, parse the returned data to change the contents of the text control of the interface through the handler, and display the recognition results.

Timer function: add ChronometerTickListener to realize the countdown function, but the pause function has not been realized yet. Set the timer for each round for 30s, and automatically submit the content when the timer ends.

![game description](/pics&video/8.png)

![game description](/pics&video/13.jpg)

Game breakthrough mode
Click on the start button to display the submit button.

Timer: 30s for each round, automatically submit the drawing board when the timer ends.

Save the drawing: click the submit button, first save the drawing board content to local machine, then convert the image to white line content on black background through binary processing, cut the size, and call recognition interface to upload to the server for recognition.

Recognition return: parse the returned data, take out the content of the recognition result, pass it to the text control through the handler, and display it in the current interface.

Points: compare the contents of the recognition with the current topic, if correct plus 10 points, if wrong no points deducted, continue to the next level. It will call the interface to get points and the interface to modify user information.

![game description](/pics&video/9.png)

Deploy back-end servers and algorithm servers to the public network to facilitate data acquisition and modification on the Android side.

![game description](/pics&video/11.png)

Print the recognition results returned by the algorithm server.

