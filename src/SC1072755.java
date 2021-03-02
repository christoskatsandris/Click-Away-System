import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

public class SC1072755 {
	public static Shop shop;
	public static AppGUI gui;
	public static Buyer buyer = null;
	public static Seller seller = null;
	
	public static void main (String[] args) {
		shop = new Shop();
		gui = new AppGUI();
	}
	
	public static void createProspectiveBuyer () {
		buyer = new Buyer();
		System.out.println("Buyer created successfully.");
	}
	
	public static void createSeller () {
		seller = new Seller();
		System.out.println("Seller created successfully.");
	}
	
	public static void runClickAway () {
		try {
			Thread tbuyer = new Thread (buyer);
			Thread tseller = new Thread (seller);
			tbuyer.setPriority(Thread.MAX_PRIORITY);
			tseller.setPriority(Thread.MIN_PRIORITY);
			tbuyer.start();
			tseller.start();
		} catch (NullPointerException e) {
			System.out.println("Objects are not initialized.");
		}
	}
}

class Buyer implements Runnable{
	@Override
	public void run () {
		System.out.println("Buyer started running.");
		for (int iterate=0; iterate<45; iterate++) {
			try {
				AppointmentRequest newRequest = new AppointmentRequest (new Date(), iterate, 5);
				newRequest.report();
				SC1072755.shop.addRequest(newRequest);
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception on class buyer.");
			}
		}
		System.out.println("Buyer completed.");
	}
}

class Seller implements Runnable {
	@Override
	public void run () {
		System.out.println("Seller started running.");
		for (int iterate=0; iterate<45; iterate++) {
			try {
				executeRequest();
				Thread.sleep(50);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("No more requests.");
			} catch (InterruptedException e) {
				System.out.println("Interrupted exception on class seller.");
			}
		}
		System.out.println("Seller completed.");
	}
	
	public void executeRequest () throws ArrayIndexOutOfBoundsException {
		if (SC1072755.shop.requests.size() == 0) throw new ArrayIndexOutOfBoundsException();
		AppointmentRequest request = SC1072755.shop.requests.get(0);
		request.report();
		SC1072755.shop.requests.remove(0);
	}
}

class Shop {
	ArrayList<AppointmentRequest> requests = new ArrayList<AppointmentRequest>();
	
	public void addRequest (AppointmentRequest request) {
		requests.add(request);
	}
}

class AppointmentRequest {
	Date date;
	long ID;
	int attendees;
	
	AppointmentRequest (Date date, long ID, int attendees) {
		this.date = date;
		this.ID = ID;
		this.attendees = attendees;
	}
	
	void report () {
		System.out.println("Request #" + Long.toString(this.ID) + " with " + Integer.toString(this.attendees) + " attendees.");
		
	}
}

class AppGUI extends Frame {
    private static final long serialVersionUID = 1L;
    Button [] buttons = new Button [5];
    public TextArea textArea;
    Font normalFont, headerFont;
    
    public AppGUI () {
        normalFont = new Font("Segoe UI", Font.PLAIN, 14);
        headerFont = new Font("Segoe UI", Font.BOLD, 20);
        prepareWindow();
        addHeader();
        addButtons();        
        setVisible(true);
    }

    void prepareWindow () {
        setBounds(100,100,800,800);
        setFont(normalFont);
        setLayout(null);
        setResizable(false);
        toFront();
        this.addWindowListener(new WindowAdapter () {
            public void windowClosing (WindowEvent e) {
                System.exit(0);
            }
        });
    }

    void addHeader () {
        Label header = new Label ("Click Away");
        header.setBounds(400, 50, 200, 50);
        header.setAlignment(Label.CENTER);
        header.setFont(headerFont);
        add(header);
    }

    void addButtons () {
        buttonCreate("Create Prospective Buyer", 20, 100, 200, 30, "createProspectiveBuyer");
        buttonCreate("Create Seller", 20, 150, 200, 30, "createSeller");
        buttonCreate("Run Click Away", 20, 200, 200, 30, "runClickAway");      
        buttonCreate("Close", 850, 150, 100, 100, "systemExit");        
    }

    void buttonCreate (String label, int x, int y, int width, int height, String labelForActionListener) {
        Button button = new Button (label);
        button.setBounds(x, y, width, height);
        button.addActionListener(new ButtonHandler(button, labelForActionListener));
        add(button);
    }
}

class ButtonHandler implements ActionListener {
    static int order = 0;
    Button buttonPressed;
    String label;
    ButtonHandler (Button buttonPressed, String label) {
        this.buttonPressed = buttonPressed;
        this.label = label;
    }
    public void actionPerformed (ActionEvent e) {
        switch (label) {
            case "createProspectiveBuyer": SC1072755.createProspectiveBuyer(); break;
            case "createSeller": SC1072755.createSeller(); break;
            case "runClickAway": SC1072755.runClickAway(); break;
            case "systemExit": System.exit(0); break;
            default: break;
        }    
    }
}