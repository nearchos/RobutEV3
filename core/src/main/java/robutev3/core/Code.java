package robutev3.core;

/**
 * Inherited from J4EV3 - https://github.com/LLeddy/J4EV3/blob/master/J4EV3/src/com/j4ev3/core/Command.java
 * This will be restructured/refactored/removed in a later version.
 */
class Code {

	//used for master/slave operation with other bricks. not necessary.
	public static byte LAYER						 = (byte) 0x00;

	public static byte opError        		         = (byte) 0x00;   // VM
	public static byte opNop                    	 = (byte) 0x01;
	public static byte opProgram_Stop           	 = (byte) 0x02;
	public static byte opProgram_Start          	 = (byte) 0x03;
	public static byte opObject_Stop            	 = (byte) 0x04;
	public static byte opObject_Start           	 = (byte) 0x05;
	public static byte opObject_Trig            	 = (byte) 0x06;
	public static byte opObject_Wait            	 = (byte) 0x07;
	public static byte opObject_Return          	 = (byte) 0x08;
	public static byte opObject_Call            	 = (byte) 0x09;
	public static byte opObject_End             	 = (byte) 0x0A;
	public static byte opSleep                  	 = (byte) 0x0B;
	public static byte opProgram_Info           	 = (byte) 0x0C;
	// PROGRAM_INFO SUBCODES
	public static byte OBJ_STOP                 	 = (byte) 0x00;
	public static byte OBJ_START                	 = (byte) 0x04;
	public static byte GET_STATUS               	 = (byte) 0x16;
	public static byte GET_SPEED                	 = (byte) 0x17;
	public static byte GET_PRGRESULT            	 = (byte) 0x18;
	public static byte SET_INSTR                	 = (byte) 0x19;

	public static byte opLabel                  	 = (byte) 0x0D;
	public static byte opProbe                  	 = (byte) 0x0E;
	public static byte opDo                     	 = (byte) 0x0F;
	public static byte opAdd8                   	 = (byte) 0x10;   // MATH
	public static byte opAdd16                  	 = (byte) 0x11;
	public static byte opAdd32                  	 = (byte) 0x12;
	public static byte opAddf                   	 = (byte) 0x13;
	public static byte opSub8                   	 = (byte) 0x14;
	public static byte opSub16                  	 = (byte) 0x15;
	public static byte opSub32                  	 = (byte) 0x16;
	public static byte opSubf                   	 = (byte) 0x17;
	public static byte opMul8                   	 = (byte) 0x18;
	public static byte opMul16                  	 = (byte) 0x19;
	public static byte opMul32                  	 = (byte) 0x1A;
	public static byte opMulf                   	 = (byte) 0x1B;
	public static byte opDiv8                   	 = (byte) 0x1C;
	public static byte opDiv16                  	 = (byte) 0x1D;
	public static byte opDiv32                  	 = (byte) 0x1E;
	public static byte opDivf                   	 = (byte) 0x1F;
	public static byte opOr8                    	 = (byte) 0x20;   // LOGIC
	public static byte opOr16                   	 = (byte) 0x21;
	public static byte opOr32                   	 = (byte) 0x22;
	public static byte opAnd8                   	 = (byte) 0x24;
	public static byte opAnd16                  	 = (byte) 0x25;
	public static byte opAnd32                  	 = (byte) 0x26;
	public static byte opXor8                   	 = (byte) 0x28;
	public static byte opXor16                  	 = (byte) 0x29;
	public static byte opXor32                  	 = (byte) 0x2A;
	public static byte opRl8                    	 = (byte) 0x2C;
	public static byte opRl16                   	 = (byte) 0x2D;
	public static byte opRl32                   	 = (byte) 0x2E;
	public static byte opInit_Bytes             	 = (byte) 0x2F;   // MOVE
	public static byte opMove8_8                	 = (byte) 0x30;
	public static byte opMove8_16               	 = (byte) 0x31;
	public static byte opMove8_32               	 = (byte) 0x32;
	public static byte opMove8_F                	 = (byte) 0x33;
	public static byte opMove16_8               	 = (byte) 0x34;
	public static byte opMove16_16              	 = (byte) 0x35;
	public static byte opMove16_32              	 = (byte) 0x36;
	public static byte opMove16_F               	 = (byte) 0x37;
	public static byte opMove32_8               	 = (byte) 0x38;
	public static byte opMove32_16              	 = (byte) 0x39;
	public static byte opMove32_32              	 = (byte) 0x3A;
	public static byte opMove32_F               	 = (byte) 0x3B;
	public static byte opMovef_8                	 = (byte) 0x3C;
	public static byte opMovef_16               	 = (byte) 0x3D;
	public static byte opMovef_32               	 = (byte) 0x3E;
	public static byte opMovef_F                	 = (byte) 0x3F;
	public static byte opJr                     	 = (byte) 0x40;   // BRANCH
	public static byte opJr_False               	 = (byte) 0x41;
	public static byte opJr_True                	 = (byte) 0x42;
	public static byte opJr_Nan                 	 = (byte) 0x43;
	public static byte opCp_Lt8                 	 = (byte) 0x44;   // COMPARE
	public static byte opCp_Lt16                	 = (byte) 0x45;
	public static byte opCp_Lt32                	 = (byte) 0x46;
	public static byte opCp_Ltf                 	 = (byte) 0x47;
	public static byte opCp_Gt8                 	 = (byte) 0x48;
	public static byte opCp_Gt16                	 = (byte) 0x49;
	public static byte opCp_Gt32                	 = (byte) 0x4A;
	public static byte opCp_Gtf                 	 = (byte) 0x4B;
	public static byte opCp_Eq8                 	 = (byte) 0x4C;
	public static byte opCp_Eq16                	 = (byte) 0x4D;
	public static byte opCp_Eq32                	 = (byte) 0x4E;
	public static byte opCp_Eqf                 	 = (byte) 0x4F;
	public static byte opCp_Ne8                 	 = (byte) 0x50;
	public static byte opCp_Ne16                	 = (byte) 0x51;
	public static byte opCp_Ne32                	 = (byte) 0x52;
	public static byte opCp_Nef                 	 = (byte) 0x53;
	public static byte opCp_Lte8                	 = (byte) 0x54;
	public static byte opCp_Lte16               	 = (byte) 0x55;
	public static byte opCp_Lte32               	 = (byte) 0x56;
	public static byte opCp_Ltef                	 = (byte) 0x57;
	public static byte opCp_Gte8                	 = (byte) 0x58;
	public static byte opCp_Gte16               	 = (byte) 0x59;
	public static byte opCp_Gte32               	 = (byte) 0x5A;
	public static byte opCp_Gtef                	 = (byte) 0x5B;
	public static byte opSelect8                	 = (byte) 0x5C;   // SELECT
	public static byte opSelect16               	 = (byte) 0x5D;
	public static byte opSelect32               	 = (byte) 0x5E;
	public static byte opSelectf                	 = (byte) 0x5F;
	public static byte opSystem                 	 = (byte) 0x60;   // VM
	public static byte opPort_Cnv_Output        	 = (byte) 0x61;
	public static byte opPort_Cnv_Input         	 = (byte) 0x62;
	public static byte opNote_To_Freq           	 = (byte) 0x63;
	public static byte opJr_Lt8                 	 = (byte) 0x64;   // BRANCH
	public static byte opJr_Lt16                	 = (byte) 0x65;
	public static byte opJr_Lt32                	 = (byte) 0x66;
	public static byte opJr_Ltf                 	 = (byte) 0x67;
	public static byte opJr_Gt8                 	 = (byte) 0x68;
	public static byte opJr_Gt16                	 = (byte) 0x69;
	public static byte opJr_Gt32                	 = (byte) 0x6A;
	public static byte opJr_Gtf                 	 = (byte) 0x6B;
	public static byte opJr_Eq8                 	 = (byte) 0x6C;
	public static byte opJr_Eq16                	 = (byte) 0x6D;
	public static byte opJr_Eq32                	 = (byte) 0x6E;
	public static byte opJr_Eqf                 	 = (byte) 0x6F;
	public static byte opJr_Neq8                	 = (byte) 0x70;
	public static byte opJr_Neq16               	 = (byte) 0x71;
	public static byte opJr_Neq32               	 = (byte) 0x72;
	public static byte opJr_Neqf                	 = (byte) 0x73;
	public static byte opJr_Lteq8               	 = (byte) 0x74;
	public static byte opJr_Lteq16              	 = (byte) 0x75;
	public static byte opJr_Lteq32              	 = (byte) 0x76;
	public static byte opJr_Lteqf               	 = (byte) 0x77;
	public static byte opJr_Gteq8               	 = (byte) 0x78;
	public static byte opJr_Gteq16              	 = (byte) 0x79;
	public static byte opJr_Gteq32              	 = (byte) 0x7A;
	public static byte opJr_Gteqf               	 = (byte) 0x7B;
	public static byte opInfo                   	 = (byte) 0x7C;   // VM
	// INFO_SUBCODE
	public static byte SET_ERROR                     = (byte) 0x01;
	public static byte GET_ERROR                     = (byte) 0x02;
	public static byte ERRORTEXT                     = (byte) 0x03;
	public static byte GET_VOLUME                    = (byte) 0x04;
	public static byte SET_VOLUME                    = (byte) 0x05;
	public static byte GET_MINUTES                   = (byte) 0x06;
	public static byte SET_MINUTES                   = (byte) 0x07;

	public static byte opStrings                     = (byte) 0x7D;
	// STRINGS_SUBCODE
	public static byte GET_SIZE                      = (byte) 0x01;
	public static byte ADD                           = (byte) 0x02;
	public static byte COMPARE                       = (byte) 0x03;
	public static byte DUPLICATE                     = (byte) 0x05;
	public static byte VALUE_TO_STRING               = (byte) 0x06;
	public static byte STRING_TO_VALUE               = (byte) 0x07;
	public static byte STRIP                         = (byte) 0x08;
	public static byte NUMBER_TO_STRING              = (byte) 0x09;
	public static byte SUB                           = (byte) 0x0A;
	public static byte VALUE_FORMATTED               = (byte) 0x0B;
	public static byte NUMBER_FORMATTED              = (byte) 0x0C;

	public static byte opMemory_Write                = (byte) 0x7E;
	public static byte opMemory_Read                 = (byte) 0x7F;
	public static byte opUI_Flush                    = (byte) 0x80;   // UI
	public static byte opUI_Read                     = (byte) 0x81;
	// UI_READ_SUBCODES
	public static byte GET_VBATT                     = (byte) 0x01;
	public static byte GET_IBATT                     = (byte) 0x02;
	public static byte GET_OS_VERS                   = (byte) 0x03;
	public static byte GET_EVENT                     = (byte) 0x04;
	public static byte GET_TBATT                     = (byte) 0x05;
	public static byte GET_IINT                      = (byte) 0x06;
	public static byte GET_IMOTOR                    = (byte) 0x07;
	public static byte GET_STRING                    = (byte) 0x08;
	public static byte GET_HW_VERS                   = (byte) 0x09;
	public static byte GET_FW_VERS                   = (byte) 0x0A;
	public static byte GET_FW_BUILD                  = (byte) 0x0B;
	public static byte GET_OS_BUILD                  = (byte) 0x0C;
	public static byte GET_ADDRESS                   = (byte) 0x0D;
	public static byte GET_CODE                      = (byte) 0x0E;
	public static byte KEY                           = (byte) 0x0F;
	public static byte GET_SHUTDOWN                  = (byte) 0x10;
	public static byte GET_WARNING                   = (byte) 0x11;
	public static byte GET_LBATT                     = (byte) 0x12;
	public static byte TEXTBOX_READ                  = (byte) 0x15;
	public static byte GET_VERSION                   = (byte) 0x1A;
	public static byte GET_IP                        = (byte) 0x1B;
	public static byte GET_POWER                     = (byte) 0x1D;
	public static byte GET_SDCARD                    = (byte) 0x1E;
	public static byte GET_USBSTICK                  = (byte) 0x1F;

	public static byte opUI_Write                    = (byte) 0x82;
	// UI_WRITE_SUBCODES
	public static byte WRITE_FLUSH                   = (byte) 0x01;
	public static byte FLOATVALUE                    = (byte) 0x02;
	public static byte STAMP                         = (byte) 0x03;
	public static byte PUT_STRING                    = (byte) 0x08;
	public static byte VALUE8                        = (byte) 0x09;
	public static byte VALUE16                       = (byte) 0x0A;
	public static byte VALUE32                       = (byte) 0x0B;
	public static byte VALUEF                        = (byte) 0x0C;
	public static byte ADDRESS                       = (byte) 0x0D;
	public static byte CODE                          = (byte) 0x0E;
	public static byte DOWNLOAD_END                  = (byte) 0x0F;
	public static byte SCREEN_BLOCK                  = (byte) 0x10;
	public static byte TEXTBOX_APPEND                = (byte) 0x15;
	public static byte SET_BUSY                      = (byte) 0x16;
	public static byte SET_TESTPIN                   = (byte) 0x17;
	public static byte INIT_RUN                      = (byte) 0x18;
	public static byte UPDATE_RUN                    = (byte) 0x1A;
	public static byte LED                           = (byte) 0x1B;
	public static byte POWER                         = (byte) 0x1D;
	public static byte GRAPH_SAMPLE                  = (byte) 0x1E;
	public static byte TERMINAL                      = (byte) 0x1F;

	public static byte opUI_Button                   = (byte) 0x83;
	// UI_BUTTON_SUBCODES
	public static byte SHORTPRESS                    = (byte) 0x01;
	public static byte LONGPRESS                     = (byte) 0x02;
	public static byte WAIT_FOR_PRESS                = (byte) 0x03;
	public static byte FLUSH                         = (byte) 0x04;
	public static byte PRESS                         = (byte) 0x05;
	public static byte RELEASE                       = (byte) 0x06;
	public static byte GET_HORZ                      = (byte) 0x07;
	public static byte GET_VERT                      = (byte) 0x08;
	public static byte PRESSED                       = (byte) 0x09;
	public static byte SET_BACK_BLOCK                = (byte) 0x0A;
	public static byte GET_BACK_BLOCK                = (byte) 0x0B;
	public static byte TESTSHORTPRESS                = (byte) 0x0C;
	public static byte TESTLONGPRESS                 = (byte) 0x0D;
	public static byte GET_BUMBED                    = (byte) 0x0E;
	public static byte GET_CLICK                     = (byte) 0x0F;

	public static byte opUI_Draw                     = (byte) 0x84;
	// UI_DRAW_SUBCODES
	public static byte UPDATE                        = (byte) 0x00;
	public static byte CLEAN                         = (byte) 0x01;
	public static byte PIXEL                         = (byte) 0x02;
	public static byte LINE                          = (byte) 0x03;
	public static byte CIRCLE                        = (byte) 0x04;
	public static byte TEXT                          = (byte) 0x05;
	public static byte ICON                          = (byte) 0x06;
	public static byte PICTURE                       = (byte) 0x07;
	public static byte VALUE                         = (byte) 0x08;
	public static byte FILLRECT                      = (byte) 0x09;
	public static byte RECT                          = (byte) 0x0A;
	public static byte NOTIFICATION                  = (byte) 0x0B;
	public static byte QUESTION                      = (byte) 0x0C;
	public static byte KEYBOARD                      = (byte) 0x0D;
	public static byte BROWSE                        = (byte) 0x0E;
	public static byte VERTBAR                       = (byte) 0x0F;
	public static byte INVERSERECT                   = (byte) 0x10;
	public static byte SELECT_FONT                   = (byte) 0x11;
	public static byte TOPLINE                       = (byte) 0x12;
	public static byte FILLWINDOW                    = (byte) 0x13;
	public static byte SCROLL                        = (byte) 0x14;
	public static byte DOTLINE                       = (byte) 0x15;
	public static byte VIEW_VALUE                    = (byte) 0x16;
	public static byte VIEW_UNIT                     = (byte) 0x17;
	public static byte FILLCIRCLE                    = (byte) 0x18;
	public static byte STORE                         = (byte) 0x19;
	public static byte RESTORE                       = (byte) 0x1A;
	public static byte ICON_QUESTION                 = (byte) 0x1B;
	public static byte BMPFILE                       = (byte) 0x1C;
	public static byte POPUP                         = (byte) 0x1D;
	public static byte GRAPH_SETUP                   = (byte) 0x1E;
	public static byte GRAPH_DRAW                    = (byte) 0x1F;
	public static byte TEXTBOX                       = (byte) 0x20;

	public static byte opTimer_Wait                  = (byte) 0x85;   // TIMER
	public static byte opTimer_Ready                 = (byte) 0x86;
	public static byte opTimer_Read                  = (byte) 0x87;
	public static byte opBp0                         = (byte) 0x88;   // VM
	public static byte opBp1                         = (byte) 0x89;
	public static byte opBp2                         = (byte) 0x8A;
	public static byte opBp3                         = (byte) 0x8B;
	public static byte opBp_Set                      = (byte) 0x8C;
	public static byte opMath                        = (byte) 0x8D;
	public static byte opRandom                      = (byte) 0x8E;
	public static byte opTimer_Read_Us               = (byte) 0x8F;   // TIMER
	public static byte opKeep_Alive                  = (byte) 0x90;   // UI
	public static byte opCom_Read                    = (byte) 0x91;   // COM
	// COM_READ_SUBCODES
	public static byte COMMAND                       = (byte) 0x0E;

	public static byte opCom_Write                   = (byte) 0x92;
	// COM_WRITE_SUBCODES
	public static byte REPLY                         = (byte) 0x0E;

	public static byte opSound                       = (byte) 0x94;   // SOUND
	// SOUND_SUBCODES
	public static byte BREAK                         = (byte) 0x00;
	public static byte TONE                          = (byte) 0x01;
	public static byte PLAY                          = (byte) 0x02;
	public static byte REPEAT                        = (byte) 0x03;
	public static byte SERVICE                       = (byte) 0x04;

	public static byte opSound_Test                  = (byte) 0x95;
	public static byte opSound_Ready                 = (byte) 0x96;
	public static byte opInput_Sample                = (byte) 0x97;
	public static byte opInput_Device_List           = (byte) 0x98;
	public static byte opInput_Device                = (byte) 0x99;
	// INPUT_DEVICE_SUBCODES
	public static byte GET_FORMAT                    = (byte) 0x02;
	public static byte CAL_MINMAX                    = (byte) 0x03;
	public static byte CAL_DEFAULT                   = (byte) 0x04;
	public static byte GET_TYPEMODE                  = (byte) 0x05;
	public static byte GET_SYMBOL                    = (byte) 0x06;
	public static byte CAL_MIN                       = (byte) 0x07;
	public static byte CAL_MAX                       = (byte) 0x08;
	public static byte SETUP                         = (byte) 0x09;
	public static byte CLR_ALL                       = (byte) 0x0A;
	public static byte GET_RAW                       = (byte) 0x0B;
	public static byte GET_CONNECTION                = (byte) 0x0C;
	public static byte STOP_ALL                      = (byte) 0x0D;
	public static byte GET_NAME                      = (byte) 0x15;
	public static byte GET_MODENAME                  = (byte) 0x16;
	public static byte SET_RAW                       = (byte) 0x17;
	public static byte GET_FIGURES                   = (byte) 0x18;
	public static byte GET_CHANGES                   = (byte) 0x19;
	public static byte CLR_CHANGES                   = (byte) 0x1A;
	public static byte READY_PCT                     = (byte) 0x1B;
	public static byte READY_RAW                     = (byte) 0x1C;
	public static byte READY_SI                      = (byte) 0x1D;
	public static byte GET_MINMAX                    = (byte) 0x1E;
	public static byte GET_BUMPS                     = (byte) 0x1F;

	public static byte opInput_Read                  = (byte) 0x9A;
	public static byte opInput_Test                  = (byte) 0x9B;
	public static byte opInput_Ready                 = (byte) 0x9C;
	public static byte opInput_ReadSI                = (byte) 0x9D;
	public static byte opInput_ReadExt               = (byte) 0x9E;
	public static byte opInput_Write                 = (byte) 0x9F;
	public static byte opOutput_Get_Type             = (byte) 0xA0;   // OUTPUT
	public static byte opOutput_Set_Type             = (byte) 0xA1;
	public static byte opOutput_Reset                = (byte) 0xA2;
	public static byte opOutput_Stop                 = (byte) 0xA3;
	public static byte opOutput_Power                = (byte) 0xA4;
	public static byte opOutput_Speed                = (byte) 0xA5;
	public static byte opOutput_Start                = (byte) 0xA6;
	public static byte opOutput_Polarity             = (byte) 0xA7;
	public static byte opOutput_Read                 = (byte) 0xA8;
	public static byte opOutput_Test                 = (byte) 0xA9;
	public static byte opOutput_Ready                = (byte) 0xAA;
	public static byte opOutput_Position             = (byte) 0xAB;
	public static byte opOutput_Step_Power           = (byte) 0xAC;
	public static byte opOutput_Time_Power           = (byte) 0xAD;
	public static byte opOutput_Step_Speed           = (byte) 0xAE;
	public static byte opOutput_Time_Speed           = (byte) 0xAF;
	public static byte opOutput_Step_Sync            = (byte) 0xB0;
	public static byte opOutput_Time_Sync            = (byte) 0xB1;
	public static byte opOutput_Clr_Count            = (byte) 0xB2;
	public static byte opOutput_Get_Count            = (byte) 0xB3;
	public static byte opOutput_Prg_Stop             = (byte) 0xB4;


	public static byte DIRECT_COMMAND_REPLY  			= (byte) 0x00;
	public static byte DIRECT_COMMAND_NO_REPLY  	 	= (byte) 0x80;
	public static byte DIRECT_REPLY          	     	= (byte) 0x02;
	public static byte DIRECT_REPLY_ERROR	         	= (byte) 0x04;
	public static byte SYSTEM_COMMAND_REPLY		 		= (byte) 0x01;
	public static byte SYSTEM_COMMAND_NO_REPLY		 	= (byte) 0x81;
	public static byte SYSTEM_REPLY				 		= (byte) 0x03;
	public static byte SYSTEM_REPLY_ERROR			 	= (byte) 0x05;

	public static int SENSOR_TYPE_TOUCH                 = 8;
	public static int SENSOR_MODE_TOUCH_TOUCH           = 0;
	public static int SENSOR_MODE_TOUCH_BUMPS           = 1;

	public static int SENSOR_TYPE_COLOR                 = 29;
	public static int SENSOR_MODE_COLOR_REFLECT         = 0;
	public static int SENSOR_MODE_COLOR_AMBIENT         = 1;
	public static int SENSOR_MODE_COLOR_COLOR           = 2;
//    public static int COLOR_REFLECTED_RAW   = 3;
//    public static int COLOR_RGB_RAW         = 4;
//    public static int COLOR_CALIBRATION     = 5;

	public static int SENSOR_TYPE_ULTRASONIC			= 30;
	public static int SENSOR_MODE_ULTRASONIC_CM   		= 0;
	public static int SENSOR_MODE_ULTRASONIC_INCH  		= 1;
	public static int SENSOR_MODE_ULTRASONIC_LISTEN		= 2;
	public static int SENSOR_MODE_ULTRASONIC_SI_CM 		= 3;
	public static int SENSOR_MODE_ULTRASONIC_SI_INCH	= 4;
	public static int SENSOR_MODE_ULTRASONIC_DC_CM  	= 5;
	public static int SENSOR_MODE_ULTRASONIC_DC_INCH	= 6;

}