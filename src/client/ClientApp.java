package clientapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author Abanoub Kamal
 */
public class ClientApp extends Application{
    
//    TextArea myTextArea;
    Label messageLabel;
    FlowPane messages;
    Label myLabel;
    TextField messageField;
    Button sendBut;
    FlowPane flowPane;
    BorderPane rootPane;  
    Scene myScene;

    Socket clientSocket;
    DataInputStream inS;
    PrintStream outS;
    Thread updatingGuiThread;

    @Override
    public void init(){
//        myTextArea = new TextArea();
        messageLabel = new Label("Chat Messages:");
        CornerRadii radii;
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setPadding(new Insets(5));
        messageLabel.setFont(javafx.scene.text.Font.font("Verdana", FontWeight.BOLD, 15));
        messageLabel.setTranslateX(3);
        messageLabel.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        myLabel = new Label("Enter your message");
        messageField = new TextField();
        messageField.setPromptText("Enter your message");
        sendBut = new Button("Send");
        sendBut.setDefaultButton(true);
        flowPane = new FlowPane(myLabel, messageField, sendBut);
        rootPane = new BorderPane();
//        rootPane.setCenter(myTextArea);
        messages = new FlowPane();
        messages.getChildren().add(messageLabel);
        rootPane.setCenter(messages);
        rootPane.setBottom(flowPane);

        myScene = new Scene(rootPane, 375, 400);

        try{            
            clientSocket = new Socket("192.168.133.1", 5000);

            inS = new DataInputStream(clientSocket.getInputStream());
            outS = new PrintStream(clientSocket.getOutputStream());
        }catch(Exception e){
            System.out.println("clientapp.ClientApp.init().init client socket and streams");
        }

    }

    @Override
    public void start(Stage primaryStage) {

//        myTextArea.appendText("Chat Messages:\n");

        sendBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    outS.println(messageField.getText());
                    messageField.clear();
                }catch(Exception e){
                    System.out.println("ClientApp.handle().write on the stream");
                }
            }
        });

        
        
        primaryStage.setTitle("Chat Client2");
        primaryStage.setScene(myScene);
        primaryStage.show();
        
        startThreadToUpdateClientGui();

    }
    
    /* Actions taken when the client app closed */
    @Override
    public void stop() throws IOException
    {
        /* close the input and output streams of the client */
        inS.close();
        outS.close();
        /* close the socket of the client */
        clientSocket.close();
        /* close the thread resposible for make changes on the GUI */
        endThreadThatUpdateClientGui();
        /* terminate the  JavaFX application explicitly */
        Platform.exit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    
    
    /**************************************************************************/
    /**************** thread to renew the data of client screen ***************/
    Thread updatingClientGuiThread;
    private void startThreadToUpdateClientGui()
    {
            Runnable runnable = new Runnable(){
            @Override
            public void run(){
                while(true){
                    try{
                        String replyMsg = inS.readLine();
//                        myTextArea.appendText("\n"+replyMsg);
                        Platform.runLater(new Runnable(){
                        @Override
                            public void run(){
                                messages.getChildren().add(new Label(replyMsg));
//                                messageLabel.setText(replyMsg);
                        }
                    });
                        
                    }catch(Exception e){
                        System.out.println("ClientApp.run().read from the stream");
                        try{
                            inS.close();
                            outS.close();
                            clientSocket.close();
                        }catch(IOException ex){}
                    }
                }
            }
        };
        updatingClientGuiThread = new Thread(runnable);
        updatingClientGuiThread.start();
    }
    
    private void endThreadThatUpdateClientGui()
    {
        updatingClientGuiThread.stop();
    }
    
}


