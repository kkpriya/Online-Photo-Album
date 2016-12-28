# Online-Photo-Album

Builds an online photo album application that access photos from flickr.com

1. User clicks Test button
  Interpret the text in the left textbox as a url, get the image at that url Scale the image to height=200 pixels, while preserving aspect
  ratio Add the image to the scrollpane 
 
2. User clicks Search button
  The Flickr photo database is searched for the tag displayed in the left textbox. The number of search results returned is no more than 
  the number entered in the right textbox Scale each search result image to height=200 pixels, while preserving aspect ratio 
  All returned search results are added to and displayed on the scrollpane at the top of the window, below the previously 
  displayed photos. The URL of each photo is printed on the console 

3. User clicks Save button 
  URLs of all photos currently displayed in the scrollpane are saved in a file (this is the saved “photo album”). The URL of each saved
  photo is printed on the console   
  
4. User clicks Exit button 
  Application exits 
  
5. User clicks Load button 
  Load photos from the previously saved photo album (actually a list of saved URLs). Add and display each loaded photo on the scrollpane 
  The URL of each loaded photo is printed on the console 
  
6. User clicks on a photo
  Photo is selected The index of the selected photo (i.e., its order in the photos displayed in the scrollpane, from top to bottom, 
  starting at 0) is printed on the console (ex., “Selected photo 3”) 
  
7. User clicks Delete button 
  Selected photo is deleted (i.e., no longer displayed in scrollpane)  The index of the deleted photo is printed on the console
  (ex. “Deleted photo 4”) 
