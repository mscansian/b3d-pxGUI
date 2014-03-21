Const UseOddScreen = 1 ;Change to 0 if you are having MAV errors

;Start Graphics
If UseOddScreen Then Graphics 300,430,16,2:Else:Graphics 640,480,16,2
AppTitle "pxGUI - Example 1"
SetBuffer BackBuffer()
Text 150,215,"Loading pxGUI, please wait.",1,1:Flip ;Write a loading message

;Include pxGUI
Include "../pxGUI.bb"
;Because the API are in diferent diretory from your program, you need to set a path to the root of API
PX_SetRoot("../")

;Set Background color to 175,175,175, and Clear the screen
PX_SetDefaultBackGround(True)
PX_LoadFonts()

;Create Gadgets
Input2        = PX_Password    (25,30,"This is a test",250,20,"Field 1:")
Input1%       = PX_Text        (25,80,"This is a test",250,20,"Field 2:")
Button1       = PX_Button      (25,120,"Copy Field 1 to Field 2",250)
Button2       = PX_Button      (25,155,"Clear Fields",250)
CheckBox1%    = PX_CheckBox    (25,190,"Enable form?",True)
ProgressBar%  = PX_ProgressBar (25,230,0,250,20,"Closing this program:")
CheckBox2%    = PX_CheckBox    (25,270,"Close it automaticaly?",False)
ProgressBar2% = PX_ProgressBar (25,320,0,250,20,"Frames Per Second: ()")
Button3       = PX_Button      (25,360,"Close",250)
Credits       = PX_Credits     (150,405,1)

While Not KeyHit(1)
		
	;Clear the screen
	Cls

	;Calculate FPS
	FramesPerSecond% = 1000/(MilliSecs()-LastFrame)
	PX_SetName(ProgressBar2%,"Frames Per Second: ("+FramesPerSecond%+")")
	PX_SetValue(ProgressBar2%,FramesPerSecond%)
	LastFrame = MilliSecs()
	
	;CheckBox2 Actions
	PX_SetState(ProgressBar% ,PX_GetValue(CheckBox2%))
	
	;CheckBox1 Actions
	PX_SetState(Input1,PX_GetValue(CheckBox1%))
	PX_SetState(Input2,PX_GetValue(CheckBox1%))
	PX_SetState(Button1,PX_GetValue(CheckBox1%))
	PX_SetState(Button2,PX_GetValue(CheckBox1%))
	
	;If button 1 is clicked
	If PX_OnClick(Button1)
		;Copy values
		PX_SetValue(Input1,PX_GetValue(Input2))
	EndIf
	
	;If button 2 is clicked
	If PX_OnClick(Button2)
		;Clear Fields
		PX_SetValue(Input1,"")
		PX_SetValue(Input2,"")
	EndIf
	
	;If button 3 is clicked
	If PX_OnClick(Button3)
		;End program
		End
	EndIf
	
	;Update Progress Bar
	PX_StepBar(ProgressBar%,0.05)
	
	;If bar reaches 100%, then end program
	If Int(PX_GetValue(ProgressBar%)) => 100 Then End

	;Update and Render Gadgets
	PX_Update()
	PX_Draw()
	
	;Switch buffers
	Flip	
Wend
End