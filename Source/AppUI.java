package openEndedProblems.removeDuplicateString.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.File;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import openEndedProblems.removeDuplicateString.algorithm.*;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import java.nio.MappedByteBuffer;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import javafx.scene.control.ProgressBar;
import java.lang.StringBuilder;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;


public class AppUI extends Application {

/************************************************************
 ************************************************************
	Background Service to load text file and store strings
	in to their respective arrays.
 ************************************************************
 ***********************************************************/

	Service<Void> loadText = new Service<Void>() {
		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() {
					new FileReader(sourceFile,sb,list);
					return null;
				}
			};
		}
	};

/************************************************************
 ************************************************************
	Displays the text in the TextArea.
	cheks the capacity of the system in terms of number of 
	cores and selects maximum number of threads and 
	sleep time.
 ************************************************************
 ***********************************************************/

	Service<Void> displayText = new Service<Void>() {
		protected Task<Void> createTask() {
			return new Task<Void> () {
				protected Void call() {
					int i = 0;
					int j = 0;
					int l = sb.length();
					short cores =(short) Runtime.getRuntime().availableProcessors();
					short sleeptime = (short)(1500/cores);
					int adder = 10;
					while (l / adder > 100) {
						adder *= 5;
					}
					System.out.println(adder + " " + l);
					counter = 0;
					FutureTask<Void> t = null;
					try {
						bar.setVisible(true);
						while (j <= sb.length()) {
							j = i + adder;
							if (j > l) {
								j = l;
							}
							updateProgress(j, l);
							Thread.sleep(sleeptime);
							t = new FutureTask<Void>(new Callable<Void>() {
								private Callable<Void> init(String s) {
									this.s = s;
									return this;
								}
								String s;
								public Void call() throws Exception {
									FileContent.appendText(s);
									counter--;
									return null;
								}
							} .init(sb.substring(i, j)));
							Platform.runLater(t);
							counter++;
							Thread.sleep(sleeptime);
							if (j == l) {
								break;
							}
							i = j - 1;
							System.out.println(counter);
							if (counter > cores) {
								t.get();
								sleeptime *= 3;
							} else {
								sleeptime = (short)(1500/cores);
							}
						

						}

						while (counter != 0) {
							t.get();

						}
						sb.delete(0, l);
					} catch (Exception err) {
						System.err.println(err.getMessage());
					}
					return null;
				}
			};
		}
	};

/************************************************************
 ************************************************************
	Background process which removes the Duplicate
	Strings from the text file and displays the
	remaining ones on the TextArea
 ************************************************************
 ***********************************************************/
	Service<Void> removeDup = new Service<Void>() {
		protected Task<Void> createTask() {
			return new Task<Void>() {
				public Void call() {
					DuplicateStringRemover dsr = new DuplicateStringRemover(list,(byte)0);
					FileContent.clear();
					dsr=null;
					sb.delete(0, sb.length());
					for (StringHash s : list) {
						sb.append(s.getString() + "\n");
					}
					int i = 0;
					int j = 0;
					int adder = 10;
					short sleeptime = 100;
					int l = sb.length();
					while (l / adder > 100) {
						adder *= 10;
					}
					counter = 0;
					bar.setVisible(true);
					try {
						while (j <= sb.length()) {
							j = i + adder;
							if (j > l) {
								j = l;
							}
							updateProgress(j, l);
							Thread.sleep(sleeptime);
							FutureTask<Void> t = new FutureTask<Void>(new Callable<Void>() {
								private Callable<Void> init(String s) {
									this.s = s;
									return this;
								}
								String s;
								public Void call() throws Exception {
									FileContent.appendText(s);
									counter--;
									return null;
								}
							} .init(sb.substring(i, j)));
							Platform.runLater(t);
							Thread.sleep(sleeptime);
							counter++;
							if (j == l) {
								break;
							}

							i = j - 1;
							if (counter > 4 ) {
								t.get();
								sleeptime *= 3;
							} else {
								sleeptime = 100;
							}

						}
						sb.delete(0, l);
					}

					catch (Exception err) {
						System.err.println(err.getMessage());
					}
					return null;
				}
			};
		}
	};


	public void init() {
		sb = new StringBuilder();
	}

	public void start(Stage myStage) {
		try {
			this.myStage = myStage;
			mainPane = new BorderPane();
			myScene = new Scene(mainPane, 550, 400);
			myScene.getStylesheets().add(getClass().getResource("Main.css").toExternalForm());
			mainPane.getStyleClass().add("mainPane");
			myStage.setTitle("Duplicate String Remover");
			myStage.setScene(myScene);
			CreateErrorBox();
			createUI();
			myStage.show();
		}

		catch (Exception err) {
			GenerateError(err.getMessage());
		}

	}


/************************************************************
 ************************************************************
	Logic required to create the UI
 ************************************************************
 ***********************************************************/

	private void createUI() {
		bar = new ProgressBar(0.0);
		bar.progressProperty().bind(loadText.progressProperty());
		bar.getStyleClass().add("progressbar");
		MenuBar mbar=new MenuBar();
		mbar.getStyleClass().add("menubar");
		Menu files=new Menu("File");
		MenuItem open=new MenuItem("Open");
		MenuItem process=new MenuItem("Process");
		mbar.getMenus().add(files);
		files.getItems().add(open);
		files.getItems().add(process);
		FileContent = new TextArea();
		FileContent.setEditable(false);
		FileContent.getStyleClass().add("textArea");
		open.setOnAction(new EventHandler < ActionEvent 	 > () {
			public void handle(ActionEvent ae) {
				try {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open Text File");
					fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
					sourceFile = fileChooser.showOpenDialog(myStage);
					if (sourceFile == null) {
						return;
					}
					if (!loadText.isRunning()) {
						loadText.reset();
						loadText.start();
					}
				} catch (Exception e) {

				}
			}
		});
		process.setOnAction(new EventHandler<ActionEvent> () {
			public void handle(ActionEvent a) {
				if (sourceFile == null) {
					GenerateError("No File Selected");
					return;
				}
				if (!removeDup.isRunning()) {
					bar.progressProperty().bind(loadText.progressProperty());
					removeDup.reset();
					removeDup.start();
				}

			}
		});

		loadText.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent w) {
				if (!displayText.isRunning()) {
					bar.progressProperty().bind(displayText.progressProperty());
					displayText.reset();
					displayText.start();
				}
			}
		});
		removeDup.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent w) {
				System.out.println("remove duplicate");
				bar.setVisible(false);
			}
		});
		displayText.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent w) {
				System.out.println("displayText");
				bar.setVisible(false);
			}
		});
		mainPane.setCenter(FileContent);
		mainPane.setTop(mbar);
		mainPane.setBottom(bar);
		bar.prefWidthProperty().bind(FileContent.widthProperty());
		bar.setVisible(false);
	}


/************************************************************
 ************************************************************
	Logic required to create Error Pop ups
 ************************************************************
 ***********************************************************/

	private void CreateErrorBox() {
		ErrorStage = new Stage();
		ErrorPane = new BorderPane();
		ErrorScene = new Scene(ErrorPane);
		ErrorLabel = new Label();
		ErrorStage.setResizable(false);
		ErrorPane.setPadding(new Insets(20, 20, 20, 20));
		ErrorStage.setScene(ErrorScene);
		ErrorPane.setTop(ErrorLabel);
		ErrorPane.setCenter(new Label(" "));
		ErrorScene.getStylesheets().add("Main.css");
		ErrorPane.getStyleClass().add("ErrorPane");
		ErrorStage.setResizable(false);
		Button ok = new Button("Ok");
		ok.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				ErrorStage.close();
			}
		});
		ErrorPane.setBottom(ok);
		ErrorPane.setAlignment(ok, Pos.BOTTOM_CENTER);
		ErrorPane.setAlignment(ErrorLabel, Pos.TOP_CENTER);
	}
/************************************************************
 ************************************************************
	Creates a popup with details about the Error Generated
	in the program
 ************************************************************
 ***********************************************************/

	private void GenerateError(String error) {
		System.out.println(error);
		ErrorLabel.setText(error);
		ErrorStage.show();

	}
	private Scene myScene;
	private Stage myStage;
	private BorderPane mainPane;
	private File sourceFile;
	private TextArea FileContent;
	private ArrayList<StringHash> list = new ArrayList<StringHash>();
	private Stage ErrorStage ;
	private BorderPane ErrorPane;
	private Scene ErrorScene;
	private Label ErrorLabel;
	private StringBuilder sb;
	private ProgressBar bar;
	private short counter;
}