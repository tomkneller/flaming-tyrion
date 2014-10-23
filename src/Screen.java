
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.glass.ui.Timer;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Screen extends Application{

	private final String NAME = "BroadChat_v0.2.2"; 			//name, add any changes add to the v no.

	protected TextArea mainTxt = new TextArea(); 					//main area where the conversation appears
	private TextField userTxt = new TextField();				//user name
	private TextField messageTxt = new TextField();				//message to send
	private final Label lbl1 = new Label();						//label for the ':' that goes in between the userTxt and messageTxt
	private Button sendBtn = new Button();						//Kind of obvious
	private final Label lbl2 = new Label();	

	//Menu bar Items
	private MenuBar menuBar = new MenuBar();

	private Menu fileMenu = new Menu("File");							//MenuBar/file
	private MenuItem saveChat1 = new MenuItem("Save Chat");
	private MenuItem loadChat1 = new MenuItem("Load Chat");
	private MenuItem fileExitMenu = new MenuItem("Exit");

	private Menu aboutMenu = new Menu("About");							//MenuBar/About
	private MenuItem aboutReadmeMenu = new MenuItem("Readme");
	private MenuItem aboutCredits = new MenuItem("Credits");
	private MenuItem aboutUpdate = new MenuItem("Check For Updates");

	//protected String textArea = null; What is this? This cannot be used anymore.

	//Networking variables.
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public Screen(){

		
	}
		
	
	public static void main(String[] args) {					
		launch(args);
	}


	@Override
	public void start(Stage primaryStage){
		
		

		BorderPane root = new BorderPane();
		Pane rootBody = new Pane();
		root.setTop(menuBar);
		root.setCenter(rootBody);
		root.setBottom(lbl2);

		Scene scene = new Scene(root,800,600);
		primaryStage.setTitle(NAME);
		primaryStage.setScene(scene);
		
		
		
		//Method in charge of setting up the networking.
		setUpNetworking();
		
		//Thread that updates the UI when a new message is sent or received.
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
		
		

		primaryStage.show();
		
		//Send button handler
		
		sendBtn.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent e) {
				
				try {
					writer.println(userTxt.getText() + ": " + messageTxt.getText());
					writer.flush();

				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				messageTxt.setText("");
				messageTxt.requestFocus();
				userTxt.setText("");
		
				
				}
			});
		
		
		
	
		
		mainTxt.setLayoutX(0);
		mainTxt.setLayoutY(0);
		mainTxt.setPrefColumnCount(66);
		mainTxt.setPrefRowCount(41);
		mainTxt.setPrefWidth(800);
		mainTxt.setPrefHeight(530);
		mainTxt.setWrapText(true);
		mainTxt.setEditable(false);
		rootBody.getChildren().add(mainTxt);
		


		// Assembling the menu bar
		fileMenu.getItems().addAll(saveChat1, new SeparatorMenuItem(), loadChat1, new SeparatorMenuItem(), fileExitMenu);
		aboutMenu.getItems().addAll(aboutReadmeMenu, aboutCredits,aboutUpdate);
		menuBar.getMenus().addAll(fileMenu, aboutMenu);

		//menu bar actions
		fileExitMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent close) {
				System.exit(0);
			}
		});
		
		saveChat1.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent c){
				new saveDialog(Screen.this);
			}
		});
		
		loadChat1.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent c){
				new loadDialog(Screen.this);
			}
		});
		
		aboutCredits.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				new creditsDialog(Screen.this);
			}

		});
		
		aboutReadmeMenu.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent a){
				new readMeDialog(Screen.this);
			}

		});
		
		aboutUpdate.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent b){
				String results = checkUpdate();
				new updateDialog(Screen.this,results);
			}

		});
	
		
		
		//main layout		

		userTxt.setLayoutX(0);
		userTxt.setLayoutY(530);
		userTxt.setPrefWidth(150);
		userTxt.setPrefColumnCount(12);
		userTxt.setText("User Name");
		rootBody.getChildren().add(userTxt);

		lbl1.setLayoutX(154);
		lbl1.setLayoutY(530);
		lbl1.setText(":");
		rootBody.getChildren().add(lbl1);

		messageTxt.setLayoutX(160);
		messageTxt.setLayoutY(530);
		messageTxt.setPrefWidth(540);
		messageTxt.setPrefColumnCount(45);
		messageTxt.setText("Message");
		rootBody.getChildren().add(messageTxt);
		
		sendBtn.setLayoutX(710);
		sendBtn.setLayoutY(530);
		sendBtn.setPrefWidth(80);
		sendBtn.setText("Send");						/**
														*
														*also try to use the broadcast address if you can, may have to code in permissions,
														*if to should be able to unicast so all IPs with a for loop judging the size of the loop by the subnet mask.
														*
														*ANTON WHAT ON EARTH ARE YOU TALKING ABOUT. A-S.
														*
														**/
		sendBtn.setDefaultButton(true);
		rootBody.getChildren().add(sendBtn); 

		
		//footer

		lbl2.setText(NAME + " was created by Anton Wolfarth, Thomas Kneller, Alexander Savill & Connor Unsworth.");	
		
		//edit the string to add your name once you have made an edit, if the string starts getting too long you should be able to change the font size with .font() i think

		
	}
	
	public String checkUpdate()
	{
		try{
			URL url = new URL("https://github.com/WolfAntian/flaming-tyrion/blob/master/README.md");
			Scanner onlineUpdate = new Scanner(url.openStream()); 	
		try{
		    File file = new File("README.md");
		    Scanner localUpdate = new Scanner(file);
		    
		    String onlineVersion = null;
		    String localVersion = null;
		    
		    while(onlineUpdate.hasNextLine())
		    {
		    	if (onlineUpdate.nextLine().contains("version:"))
		    	{
		    		onlineVersion = (onlineUpdate.nextLine());
		    	}
		    }
		    
		    while(localUpdate.hasNextLine())
		        {
		    	if (localUpdate.nextLine().contains("version:"))
		    	{
		    		localVersion = (localUpdate.nextLine());
		    	}
		        }
		    if (!onlineVersion.contains(localVersion))
		    {
		    	onlineUpdate.close();
		    	localUpdate.close();
		    	return("Unfortunately you have an old version");
		    }
		    else 
		    {
		    	onlineUpdate.close();
		    	localUpdate.close();
		    	return("Your all good! Your version is up to date.");
		    	
		    }
		}       	
		catch(Exception ex)
		    {  
			onlineUpdate.close();	
			return("Oops there has been an error. Sorry! :(");
		    }
		}
		catch(IOException downloadError)
		{	
			return("Sorry but a connection cannot be established to us.");
		}
	}
	
	/**
	 * 
	 * This method is completely dead now. Needs fixing or removing.
	 */
	//public String getText()
	//{
		//return textArea;
	//}
	
	
	
	
	//opens dialog and user can save chat to location
	class saveDialog {Screen parent;saveDialog(Screen parent){

		this.parent=parent;
		Stage dialogStage = new Stage();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(dialogStage);
		 if(selectedDirectory == null){
	         System.out.println("Error");
	     }else{
	         System.out.println(selectedDirectory.getAbsolutePath());     
	     }
		 String location = selectedDirectory.toString();
		 
		 try {

			 File newChatFile = new File(location + "/chat.chat");
			 FileWriter fileWriter = null;
			 fileWriter = new FileWriter(newChatFile);
			//////////////////////////////////////////////////// fileWriter.write(getText()); this method is broken. Another way needs to be made to save chat.
			 fileWriter.close();
			 System.out.println("File is being saved");
		 } catch (IOException e) {
			 System.out.println("Error message here");
		}
		
		
	}
	}

	
	
	
	
	
	
	//opens dialog and user can save chat to location
	class loadDialog {Screen parent;loadDialog(Screen parent){

		this.parent=parent;
		Stage dialogStage = new Stage();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("chat Files (*.chat)","*.chat");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(dialogStage);
		if(file == null){
	       System.out.println("You Havent Selected a path");
	   }else{
	      
		 try {
			 	Scanner fileReader = new Scanner(file);
			 	fileReader.useDelimiter(",");
			 	while (fileReader.hasNext()){
			 		String nextLine = fileReader.next();
			 		if (nextLine.contains("null"))
			 			nextLine = fileReader.next();
			 		mainTxt.appendText(nextLine);
			 	}
			
			 System.out.println("File is being opened");
		 } catch (IOException e) {
			 System.out.println("Error message here");
		}
	   }
		
	}
	}
	
	
	
	//Connor where is a nice description of your method explaining what it does?
	
	class creditsDialog {Screen parent;Label lblCreditsDialogHeader;Hyperlink name1;Hyperlink name2;Hyperlink name3;Hyperlink name4;creditsDialog(Screen parent){

		this.parent=parent;
		lblCreditsDialogHeader = new Label();
		lblCreditsDialogHeader.setText("Programmers");
		lblCreditsDialogHeader.setLayoutX(50);
		lblCreditsDialogHeader.setLayoutY(10);
		
		name1 = new Hyperlink("www.twitter.com");
		name1.setText("Anton Wolfarth");
		name1.setLayoutX(50);
		name1.setLayoutY(35);
		name1.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent hyperlink) 
		    {
		    	getHostServices().showDocument("https://github.com/WolfAntian");
		    }
		    });
			
		name2 = new Hyperlink();
		name2.setText("Thomas Kneller");
		name2.setLayoutX(50);
		name2.setLayoutY(60);
		name2.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent hyperlink) 
		    {
		    	getHostServices().showDocument("https://github.com/tomkneller");
		    }
		    });
		
		name3 = new Hyperlink();
		name3.setText("Alexander Savill");
		name3.setLayoutX(50);
		name3.setLayoutY(85);
		name3.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent hyperlink) 
		    {
		    	getHostServices().showDocument("https://github.com/rednaz5");
		    }
		    });
		
		name4 = new Hyperlink();
		name4.setText("Connor Unsworth");
		name4.setLayoutX(50);
		name4.setLayoutY(110);
		name4.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent hyperlink) 
		    {
		    	getHostServices().showDocument("https://github.com/ConnorUnsworth");
		    }
		    });
		
		Stage dialogStage = new Stage();
		Pane dialogRoot = new Pane();
		dialogStage.setScene(new Scene(dialogRoot, 200, 200));
		dialogStage.setTitle("Credits");
		dialogRoot.getChildren().addAll(lblCreditsDialogHeader,name1 ,name2 ,name3 ,name4);
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.show();
	}
	}
	
	
	
	
	
	
	class readMeDialog{Screen parent;TextArea rMD1;readMeDialog(Screen parent){

		this.parent=parent;
		rMD1 = new TextArea();
		try{
		    File file = new File("README.md");
		    Scanner readMeScanner = new Scanner(file);
		    while(readMeScanner.hasNextLine())
		        {
		    		rMD1.appendText(readMeScanner.nextLine() + "\n");
		        }
		    		readMeScanner.close();	
				}
		catch(Exception ex)
		    {  
				rMD1.setText("Oops. The ReadMe file seems to be missing");	
		    }
		rMD1.setLayoutX(30);
		rMD1.setLayoutY(10);
		Stage dialogStage = new Stage();
		Pane dialogRoot = new Pane();
		dialogStage.setScene(new Scene(dialogRoot, 590, 200));
		dialogStage.setTitle("ReadMe");
		dialogRoot.getChildren().add(rMD1);
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.show();
	}
	}
	
	
	
	
	class updateDialog{Screen parent;Label updateText1;Button updateButton;updateDialog(Screen parent, String results){

		this.parent=parent;
		updateText1 = new Label();
		updateText1.setLayoutX(30);
		updateText1.setLayoutY(10);
		Stage dialogStage = new Stage();
		Pane dialogRoot = new Pane();
		dialogStage.setScene(new Scene(dialogRoot, 300, 200));
		dialogStage.setTitle("Updates");
		updateText1.setText(results);
		if(results.equals("Unfortunately you have an old version"))
		{
			updateButton = new Button();
			updateButton.setLayoutX(98);
			updateButton.setLayoutY(90);
			updateButton.setPrefWidth(80);
			updateButton.setText("Download");
			updateButton.setOnAction(updateButtonHandler);							//sends to the button handler where the bulk of code will be for the output, see if you can make an event handler for the input stream of data when receiving it will add automatically,
			updateButton.setDefaultButton(true);
			dialogRoot.getChildren().add(updateButton);
		}
		
		dialogRoot.getChildren().add(updateText1);
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.show();
	}

	}
	
	//button handler
			EventHandler<ActionEvent> updateButtonHandler = new EventHandler<ActionEvent>(){
				
				@Override
				public void handle(ActionEvent arg0) {
					//dummy for presentation
					//change to send as broadcast
					//update mainTxt through receiving broadcast
					String htmlFile = new String("https://github.com/WolfAntian/flaming-tyrion/archive/master.zip");
					try{
						java.awt.Desktop.getDesktop().browse(java.net.URI.create(htmlFile));
					}
					catch(Exception ex)
					{
				         System.out.println("url error");
					}	

			}
			};
	
	
	/**
	 * Setup networking method that creates a socket and requires an IP address to connect to. The IP address you enter must
	 * be to a server and that server must have the specified port open. The server then takes the input from the writer and 
	 * sends it back to the client. This is done using chained readers. A dialog box appears informing the user of a succesful connection.
	 * 
	 * This method and the Task thread act as the main bulk of the project. Hard to imagine considering the size of it.
	 */

	private void setUpNetworking() {
		try {
			sock = new Socket("", 4550); ///////INSERT IP OF HOST HERE//////
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			JOptionPane.showMessageDialog(null, "Connection established!");
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Connection failed.");
			ex.printStackTrace();
		}
	}

	
	/**
	 * Have you ever seen Java cod so grand? Look at this beautiful thread. Un-phased by the Java FX UI thread,
	 * this thread takes things into its own hands and updates the mainTxt TextArea each time the reader reads something 
	 * from the buffer. It really is a peice of art.
	 */
	
	
	Task task = new Task<Void>() {
	    @Override
	    public Void call() throws Exception {
	    	 String message;
	       while((message = reader.readLine()) != null){
	    	   final String messagei = message;
	    	   Platform.runLater(new Runnable(){
	    	   @Override
	    	   public void run() {
	    	   mainTxt.appendText(messagei + "\n");
	    	   }
	       });
	    	   
	    	   Thread.sleep(1000);
		
	       }
	       return null;
	    }

	
	};
	
}
		



