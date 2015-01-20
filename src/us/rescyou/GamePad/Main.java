package us.rescyou.GamePad;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class Main implements SerialPortEventListener {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM7", // Windows
	};
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 19200;

	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		System.out.println("Port ID: ");
		System.out.println(portId);
		System.out.println("");
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			// System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				Robot robot = new Robot();
				// H HIGH
				if (inputLine.equals("A HIGH")) {
					System.out.println("A | HIGH");
					robot.keyPress(KeyEvent.VK_SPACE);
				}

				// H LOW
				if (inputLine.equals("A LOW")) {
					System.out.println("A | LOW");
					robot.keyRelease(KeyEvent.VK_SPACE);
				}

				// J HIGH
				if (inputLine.equals("B HIGH")) {
					System.out.println("B | HIGH");
					robot.keyPress(KeyEvent.VK_J);
				}

				// J LOW
				if (inputLine.equals("B LOW")) {
					System.out.println("B | LOW");
					robot.keyRelease(KeyEvent.VK_J);
				}

				// W HIGH
				if (inputLine.equals("UP HIGH")) {
					System.out.println("UP | HIGH");
					robot.keyPress(KeyEvent.VK_SPACE);
				}

				// W LOW
				if (inputLine.equals("UP LOW")) {
					System.out.println("UP | LOW");
					robot.keyRelease(KeyEvent.VK_SPACE);
				}

				// S HIGH
				if (inputLine.equals("DOWN HIGH")) {
					System.out.println("DOWN | HIGH");
					robot.keyPress(KeyEvent.VK_S);
				}

				// S LOW
				if (inputLine.equals("DOWN LOW")) {
					System.out.println("DOWN | LOW");
					robot.keyRelease(KeyEvent.VK_S);
				}

				// A HIGH
				if (inputLine.equals("LEFT HIGH")) {
					System.out.println("LEFT | HIGH");
					robot.keyPress(KeyEvent.VK_LEFT);
				}

				// A LOW
				if (inputLine.equals("LEFT LOW")) {
					System.out.println("LEFT | LOW");
					robot.keyRelease(KeyEvent.VK_LEFT);
				}

				// D HIGH
				if (inputLine.equals("RIGHT HIGH")) {
					System.out.println("RIGHT | HIGH");
					robot.keyPress(KeyEvent.VK_RIGHT);
				}

				// D LOW
				if (inputLine.equals("RIGHT LOW")) {
					System.out.println("RIGHT | LOW");
					robot.keyRelease(KeyEvent.VK_RIGHT);
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.initialize();
		Thread t = new Thread() {
			public void run() {
				// the following line will keep this app alive for 1000 seconds,
				// waiting for events to occur and responding to them (printing
				// incoming messages to console).
				try {
					Thread.sleep(1000000);
				} catch (InterruptedException ie) {
				}
			}
		};
		t.start();
		System.out.println("- Started -");
		System.out.println("");
	}
}

//