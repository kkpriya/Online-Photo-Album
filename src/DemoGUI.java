
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.google.gson.Gson;

class Response {
    App photos;
}

class App {
    String total;
    int page;
    int pages;
    int perpage;
    PhotoInfo [] photo;

}

class PhotoInfo {
    int isfamily;
    int ispublic;
    int isfriend;
    String id;
    int farm;
    String title;
    int context;
    String owner;
    String secret;
    String server;
    float longitude;
    float latitude;
    int accuracy;
}

public class DemoGUI extends JFrame implements ActionListener {

	JTextField searchTagField = new JTextField("");
	JTextField numResultsStr = new JTextField("10");
	JPanel onePanel;
	JScrollPane oneScrollPanel;
	JButton testButton = new JButton("Test");
	JButton searchButton = new JButton("Search");
	JButton loadButton = new JButton("Load");
	JButton deleteButton = new JButton("Delete");
	JButton saveButton = new JButton("Save");
	JButton exitButton = new JButton("Exit");
	static int frameWidth = 800;
	static int frameHeight = 600;
	ArrayList<String> loc = new ArrayList<String>();

	public DemoGUI() {

		// create bottom subpanel with buttons, flow layout
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		
		// add testButton to bottom subpanel
		buttonsPanel.add(testButton);
		// add listener for testButton clicks
		testButton.addActionListener(this);
		
		// add loadButton to bottom subpanel
		buttonsPanel.add(loadButton);
		// add listener for loadButton clicks
		loadButton.addActionListener(this);
		
		// add deleteButton to bottom subpanel
		buttonsPanel.add(deleteButton);
		// add listener for deleteButton clicks
		deleteButton.addActionListener(this);
		
		// add saveButton to bottom subpanel
		buttonsPanel.add(saveButton);
		// add listener for saveButton clicks
		saveButton.addActionListener(this);
		
		// add exitButton to bottom subpanel
		buttonsPanel.add(exitButton);
		// add listener for exitButton clicks
		exitButton.addActionListener(this);
	
		// create middle subpanel with 2 text fields and button, border layout
		JPanel textFieldSubPanel = new JPanel(new FlowLayout());
		// create and add label to subpanel
		JLabel tl = new JLabel("Enter search tag:");
		textFieldSubPanel.add(tl);

		// set width of left text field
		searchTagField.setColumns(23);
		// add listener for typing in left text field
		searchTagField.addActionListener(this);
		// add left text field to middle subpanel
		textFieldSubPanel.add(searchTagField);
		// add search button to middle subpanel
		textFieldSubPanel.add(searchButton);
		// add listener for searchButton clicks
		searchButton.addActionListener(this);
		

		// create and add label to middle subpanel, add to middle subpanel
		JLabel tNum = new JLabel("max search results:");
		numResultsStr.setColumns(2);
		textFieldSubPanel.add(tNum);
		textFieldSubPanel.add(numResultsStr);

		// create and add panel to contain bottom and middle subpanels
		/*
		 * JPanel textFieldPanel = new JPanel(new BorderLayout());
		 * textFieldPanel.add(buttonsPanel, BorderLayout.SOUTH);
		 * textFieldPanel.add(textFieldSubPanel, BorderLayout.NORTH);
		 */
		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.Y_AXIS));
		textFieldPanel.add(textFieldSubPanel);
		textFieldPanel.add(buttonsPanel);

		// create top panel
		onePanel = new JPanel();
		onePanel.setLayout(new BoxLayout(onePanel, BoxLayout.Y_AXIS));
		onePanel.addMouseListener(new MAdapter());
		// create scrollable panel for top panel
		oneScrollPanel = new JScrollPane(onePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		oneScrollPanel.setPreferredSize(new Dimension(frameWidth, frameHeight - 100));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		// add scrollable panel to main frame
		add(oneScrollPanel);

		// add panel with buttons and textfields to main frame
		add(textFieldPanel);

	}

	public static void main(String[] args) throws Exception {
		DemoGUI frame = new DemoGUI();
		frame.setTitle("Swing GUI Demo");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/*
	 * Checks if image is valid
	 */
	Image getImageURL(String loc) {
		Image img = null;
		try {
			final URL url = new URL(loc);
			img = ImageIO.read(url);
		} catch (Exception e) {
			System.out.println("Error loading image...");
			return null;
		}
		return img;
	}
	
	/*
	 * Gets the height and width of the image 
	 */
	public double aspect (Image img) {
		
		double a;
		double c = img.getHeight(null);
		double b = img.getWidth(null);
		a = b/c*200;
		return a;
	}

	/* 
	 * checks what action user has chosen: search, load, test, save, delete, exit
	 * executes the chosen action
	 */
	public void actionPerformed(ActionEvent e) {
		
		//Search for images with the given tag field
		if (e.getSource() == searchButton) {
			System.out.println("Search");

			String key = searchTagField.getText();
			String api  = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
			// number of results per page
		        String request = api + "&per_page="+numResultsStr.getText();
		        request += "&format=json&nojsoncallback=1&extras=geo";
		        request += "&api_key=" + "3da55d4dddd7f80261dc6ed130629a5c";

		        String ke = "";
		        if (key.length() != 0) {
		        	for (int j = 0; j < key.length(); j++){
		        		if (key.charAt(j) == ' '){
		        			ke += "%20";
		        		} else {
		        			ke += key.charAt(j);
		        		}
		        	}
		        } else
		        	return;
		    	if (ke.length() != 0) {
		    	    request += "&tags="+ke;
		    	} else
		    		return;

		    	System.out.println("Sending http GET request:");
		    	System.out.println(request);

		    	// open http connection
		    	URL obj = null;
				try {
					obj = new URL(request);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		            HttpURLConnection con = null;
					try {
						con = (HttpURLConnection) obj.openConnection();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

		    	// send GET request
		            try {
						con.setRequestMethod("GET");
					} catch (ProtocolException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

		    	// get response
		            int responseCode = 0;
					try {
						responseCode = con.getResponseCode();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	
		    	System.out.println("Response Code : " + responseCode);

		    	// read and construct response String
		            BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader
									       (con.getInputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            String inputLine;
		            StringBuffer response = new StringBuffer();

		            try {
						while ((inputLine = in.readLine()) != null) {
						    response.append(inputLine);
						}
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		            try {
						in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

		    	System.out.println(response);

		    	Gson gson = new Gson();
		    	String s = response.toString();

			Response responseObject = gson.fromJson(s, Response.class);
			System.out.println("# photos = " + responseObject.photos.photo.length);
			for (int i = 0; i < responseObject.photos.photo.length; i++){
				int farm = responseObject.photos.photo[i].farm;
				String server = responseObject.photos.photo[i].server;
				String id = responseObject.photos.photo[i].id;
				String secret = responseObject.photos.photo[i].secret;
				String photoUrl = "http://farm"+farm+".static.flickr.com/"
						+server+"/"+id+"_"+secret+".jpg";
				
				paint(photoUrl);
			}

		} else if (e.getSource() == testButton) {
			System.out.println("Test");
			int i = onePanel.getComponentCount();
			paint(searchTagField.getText());

		} else if (e.getSource() == searchTagField) {
			System.out.println("searchTagField: " + searchTagField.getText());
		
		} else if (e.getSource() == loadButton) {
			System.out.println("Load");		

				try (BufferedReader br = new BufferedReader(new FileReader("..\\saved.txt"))) {

					String sCurrentLine;

					while ((sCurrentLine = br.readLine()) != null) {
						System.out.println("Loading URL: " + sCurrentLine);
						paint(sCurrentLine);
						
					}

				} catch (IOException y) {
					y.printStackTrace();
				}
			
			
		} else if (e.getSource() == deleteButton) {
			if (q == -1) {
				System.out.println("Please select a picture first before deleting.");
				return;
			}
			if (loc.size()-1 >= q) {
			System.out.println("Deleted photo " + q);
			onePanel.remove(q);
			loc.remove(q);
			refresh();
			} else
				System.out.println("Select a picture first");
		
		} else if (e.getSource() == saveButton) {
			BufferedWriter write = null;
			try {
				write = new BufferedWriter(new FileWriter("saved.txt"));
				PrintWriter print_line = new PrintWriter(write);
				for (int i = 0; i < loc.size(); i++) {
				print_line.println(loc.get(i));
				}
				print_line.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("Saved");
			
		
		} else if (e.getSource() == exitButton) {
			System.out.println("Exit");
			System.exit(0);
		}
			
	}
	
	/* 
	 * Displays the image
	 */
	public void paint (String URL) {

		loc.add(URL);
		Image img = getImageURL(URL);
		double c = aspect(img);
		Image sImg = img.getScaledInstance((int) c, 200, Image.SCALE_DEFAULT);
		
		onePanel.add(new JLabel(new ImageIcon(sImg)));
		refresh();
	}
	
	/*
	 * updates each time there is a new action performed
	 */
	public void refresh () {

		onePanel.revalidate();
		onePanel.repaint();
		// connect updated onePanel to oneScrollPanel
		oneScrollPanel.setViewportView(onePanel);
	}
	
	int q = -1;
	private class MAdapter extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			Point me = e.getPoint();
			double o = me.getY()/200;
			int i = (int)o;
			if (loc.size()-1 >= i) {
				System.out.println("Selected photo: Photo #" + i);
				q = i;
			}
		}
	}
}