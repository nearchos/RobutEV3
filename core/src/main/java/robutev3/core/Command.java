package robutev3.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Inherited from J4EV3 - https://github.com/LLeddy/J4EV3/blob/master/J4EV3/src/com/j4ev3/core/Code.java
 */
class Command {

	private static int sequence = 1;

	int getSequence() {
		return sequence;
	}

	private List<Byte> ops;

	private byte commandType;

	Command(byte commandType) { // todo replace with factory
		this(commandType, 0, 0);
	}

	Command(byte commandType, int globalMemory, int localMemory) {
		if (globalMemory > 1019)
			throw new IllegalArgumentException("Global memory maximum size exceeded. 1019 bytes.");
		if (localMemory > 63)
			throw new IllegalArgumentException("Local memory maximum size exceeded. 63 bytes.");
		ops = new ArrayList<Byte>();
		this.commandType = commandType;
		// Temporary command length
		ops.add((byte) 0xFF);
		ops.add((byte) 0xFF);
		// Message counter
		if (sequence < 65535)
			sequence++;
		else
			sequence = 1;
		ops.add((byte) sequence);
		ops.add((byte) (sequence >> 8));
		// CommandType
		ops.add(commandType);
		if (commandType == Code.DIRECT_COMMAND_REPLY || commandType == Code.DIRECT_COMMAND_NO_REPLY) {
			// Global and local memory llllllgg gggggggg
			short sh = (short) (localMemory * 1024 + globalMemory);
			ops.add((byte) sh);
			ops.add((byte) (sh >> 8));
		}
	}

	byte getCommandType() {
		return commandType;
	}

	void addByte(byte b) {
		ops.add(b);
	}

	// Local Constant 1,2,4 bytes follow
	void addLCX(int value) {
		if (value >= -32 && value < 0) {
			ops.add((byte) (0x3F & (value + 64)));
		} else if (value >= 0 && value < 32) {
			ops.add((byte) value);
		} else if (value >= -127 && value <= 127) {
			ops.add((byte) 0x81);
			ops.add((byte) value);
		} else if (value >= -32767 && value <= 32767) {
			ops.add((byte) 0x82);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
		} else {
			ops.add((byte) 0x83);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
			ops.add((byte) (value >> 16));
			ops.add((byte) (value >> 24));
		}
	}

	// Local Constant string null-terminated
	void addLCS(String value) {
		ops.add((byte) 0x84);
		byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
		for (byte b : bytes)
			ops.add(b);
		ops.add((byte) 0x00);
	}

	// Local Variable 1,2,4 bytes follow
	void addLVX(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Parameter must be positive " + value);
		} else if (value < 32) {
			ops.add((byte) (0x40 | value));
		} else if (value < 256) {
			ops.add((byte) 0xc1);
			ops.add((byte) value);
		} else if (value < 65536) {
			ops.add((byte) 0xc2);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
		} else {
			ops.add((byte) 0xc3);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
			ops.add((byte) (value >> 16));
			ops.add((byte) (value >> 24));
		}
	}

	// Global Variable 1,2,4 bytes follow
	void addGVX(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Parameter must be positive " + value);
		} else if (value < 32) {
			ops.add((byte) (0x60 | value));
		} else if (value < 256) {
			ops.add((byte) 0xe1);
			ops.add((byte) value);
		} else if (value < 65536) {
			ops.add((byte) 0xe2);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
		} else {
			ops.add((byte) 0xe3);
			ops.add((byte) value);
			ops.add((byte) (value >> 8));
			ops.add((byte) (value >> 16));
			ops.add((byte) (value >> 24));
		}
	}
	
	byte [] byteCode() {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(ops.size());
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		ops.remove(0);
		ops.remove(0);
		buffer.put((byte) ops.size());
		buffer.put((byte) (ops.size() >> 8));
		for (int i = 0; i < ops.size(); i++) {
			buffer.put(ops.get(i));
		}
		byte[] cmd = new byte[buffer.position()];
		for (int i = 0; i < buffer.position(); i++) {
			cmd[i] = buffer.get(i);
		}
		return cmd;
	}

	// MOTOR
	void motorStartMotor(byte ports) {
		addByte(Code.opOutput_Start);
		addByte(Code.LAYER);
		addByte(ports);
	}

	void motorStopMotor(byte ports, boolean brake) {
		addByte(Code.opOutput_Stop);
		addByte(Code.LAYER);
		addByte(ports);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorSetPolarity(byte ports, int polarity) {
		addByte(Code.opOutput_Polarity);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(polarity);
	}

	void motorTurnAtPower(byte ports, int power) {
		addByte(Code.opOutput_Power);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(power);
	}

	void motorTurnAtSpeed(byte ports, int speed) {
		addByte(Code.opOutput_Speed);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(speed);
	}

	void motorStepAtPower(byte ports, int power, int rampUp, int continueRun, int rampDown, boolean brake) {
		addByte(Code.opOutput_Step_Power);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(power);
		addLCX(rampUp);
		addLCX(continueRun);
		addLCX(rampDown);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorTimeAtPower(byte ports, int power, int msRampUp, int msContinueRun, int msRampDown, boolean brake) {
		addByte(Code.opOutput_Time_Power);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(power);
		addLCX(msRampUp);
		addLCX(msContinueRun);
		addLCX(msRampDown);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorStepAtSpeed(byte ports, int speed, int rampUp, int continueRun, int rampDown, boolean brake) {
		addByte(Code.opOutput_Step_Speed);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(speed);
		addLCX(rampUp);
		addLCX(continueRun);
		addLCX(rampDown);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorTimeAtSpeed(byte ports, int speed, int msRampUp, int msContinueRun, int msRampDown, boolean brake) {
		addByte(Code.opOutput_Time_Speed);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(speed);
		addLCX(msRampUp);
		addLCX(msContinueRun);
		addLCX(msRampDown);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorStepSync(byte ports, int speed, int turn, int step, boolean brake) {
		addByte(Code.opOutput_Step_Sync);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(speed);
		addLCX(turn);
		addLCX(step);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	void motorTimeSync(byte ports, int speed, int turn, int msTime, boolean brake) {
		addByte(Code.opOutput_Time_Sync);
		addByte(Code.LAYER);
		addByte(ports);
		addLCX(speed);
		addLCX(turn);
		addLCX(msTime);
		addByte(brake ? (byte) 0x01 : (byte) 0x00);
	}

	// SENSOR
	void sensorGetTypeMode(byte port) {
		addByte(Code.opInput_Device);
		addByte(Code.GET_TYPEMODE);
		addByte(Code.LAYER);
		addByte(port);
		addGVX(0);
		addGVX(1);
	}

	void sensorReadyPercent(byte port, int type, int mode) {
		addByte(Code.opInput_Device);
		addByte(Code.READY_PCT);
		addByte(Code.LAYER);
		addByte(port);
		addLCX(type);
		addLCX(mode);
		addLCX(1);
		addGVX(0);
	}

	void sensorReadyRaw(byte port, int type, int mode) {
		addByte(Code.opInput_Device);
		addByte(Code.READY_RAW);
		addByte(Code.LAYER);
		addByte(port);
		addLCX(type);
		addLCX(mode);
		addLCX(1);
		addGVX(0);
	}

	void sensorReadySI(byte port, int type, int mode) {
		addByte(Code.opInput_Device);
		addByte(Code.READY_SI);
		addByte(Code.LAYER);
		addByte(port);
		addLCX(type);
		addLCX(mode);
		addLCX(1);
		addGVX(0);
	}

	void sensorGetName(byte port, int responseLength) {
		addByte(Code.opInput_Device);
		addByte(Code.GET_NAME);
		addByte(Code.LAYER);
		addByte(port);
		addLCX(responseLength);
		addGVX(0);
	}

	void sensorGetModeName(byte port, int mode, int responseLength) {
		addByte(Code.opInput_Device);
		addByte(Code.GET_MODENAME);
		addByte(Code.LAYER);
		addByte(port);
		addLCX(mode);
		addLCX(responseLength);
		addGVX(0);
	}

	void sensorGetConnectionType(byte port) {
		addByte(Code.opInput_Device);
		addByte(Code.GET_CONNECTION);
		addByte(Code.LAYER);
		addByte(port);
		addGVX(0);
	}

	public void sensorClearAll() {
		addByte(Code.opInput_Device);
		addByte(Code.CLR_ALL);
		addByte(Code.LAYER);
	}

	public void sensorClearChanges(byte port) {
		addByte(Code.opInput_Device);
		addByte(Code.CLR_CHANGES);
		addByte(Code.LAYER);
		addByte(port);
	}

	// SPEAKER
	void speakerPlayTone(int volume, int frequency, int msDuration) {
		addByte(Code.opSound);
		addByte(Code.TONE);
		addLCX(volume);
		addLCX(frequency);
		addLCX(msDuration);
	}

	void speakerPlaySound(int volume, String fileName) {
		addByte(Code.opSound);
		addByte(Code.PLAY);
		addLCX(volume);
		addLCS(fileName);
	}

	void speakerBreakPlay() {
		addByte(Code.opSound);
		addByte(Code.BREAK);
	}

	// LED
	void ledSetPattern(byte pattern) {
		addByte(Code.opUI_Write);
		addByte(Code.LED);
		addByte(pattern);
	}

	// BUTTON
	void buttonButtonPressed(byte button) {
		addByte(Code.opUI_Button);
		addByte(Code.PRESSED);
		addByte(button);
		addGVX(0);
	}

	// LCD
	void lcdUpdate() {
		addByte(Code.opUI_Draw);
		addByte(Code.UPDATE);
	}

	void lcdDrawPixel(byte color, int x, int y) {
		addByte(Code.opUI_Draw);
		addByte(Code.PIXEL);
		addByte(color);
		addLCX(x);
		addLCX(y);
	}

	void lcdDrawLine(byte color, int x0, int y0, int x1, int y1) {
		addByte(Code.opUI_Draw);
		addByte(Code.LINE);
		addByte(color);
		addLCX(x0);
		addLCX(x1);
		addLCX(y0);
		addLCX(y1);
	}

	void lcdDrawDottedLine(byte color, int x0, int y0, int x1, int y1, int onPixels, int offPixels) {
		addByte(Code.opUI_Draw);
		addByte(Code.DOTLINE);
		addByte(color);
		addLCX(x0);
		addLCX(y0);
		addLCX(x1);
		addLCX(y1);
		addLCX(onPixels);
		addLCX(offPixels);
	}

	void lcdDrawRect(byte color, int x, int y, int xSize, int ySize) {
		addByte(Code.opUI_Draw);
		addByte(Code.RECT);
		addByte(color);
		addLCX(x);
		addLCX(y);
		addLCX(xSize);
		addLCX(ySize);
	}

	void lcdFillRect(byte color, int x, int y, int xSize, int ySize) {
		addByte(Code.opUI_Draw);
		addByte(Code.FILLRECT);
		addByte(color);
		addLCX(x);
		addLCX(y);
		addLCX(xSize);
		addLCX(ySize);
	}

	void lcdDrawCircle(byte color, int x, int y, int radius) {
		addByte(Code.opUI_Draw);
		addByte(Code.CIRCLE);
		addByte(color);
		addLCX(x);
		addLCX(y);
		addLCX(radius);
	}

	void lcdFillCircle(byte color, int x, int y, int radius) {
		addByte(Code.opUI_Draw);
		addByte(Code.FILLCIRCLE);
		addLCX(x);
		addLCX(y);
		addLCX(radius);
	}

	void lcdDrawText(byte color, int x, int y, String text) {
		addByte(Code.opUI_Draw);
		addByte(Code.TEXT);
		addByte(color);
		addLCX(x);
		addLCX(y);
		addLCS(text);
	}

	void lcdDrawPicture(byte color, int x, int y, String filePath) {
		addByte(Code.opUI_Draw);
		addByte(Code.PICTURE);
		addByte(color);
		addLCX(x);
		addLCX(y);
		addLCS(filePath);
	}

	void lcdFillWindow(byte color, int y0, int ySize) {
		addByte(Code.opUI_Draw);
		addByte(Code.FILLWINDOW);
		addByte(color);
		addLCX(y0);
		addLCX(ySize);
	}

	void lcdEnableTopLine(int zeroOne) {
		addByte(Code.opUI_Draw);
		addByte(Code.TOPLINE);
		addLCX(zeroOne);
	}

	void lcdSelectFont(int font) {
		addByte(Code.opUI_Draw);
		addByte(Code.SELECT_FONT);
		addLCX(font);
	}

	// GENERAL OPERATIONS
	void genStopProgram(int prgId) {
		addByte(Code.opProgram_Stop);
		addLCX(prgId);
	}
}