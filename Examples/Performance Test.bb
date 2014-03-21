;Start Graphics
Graphics 640,480,16,2
AppTitle "pxGUI - Performance Test"
SetBuffer BackBuffer()
Text 320,240,"Loading pxGUI, please wait.",1,1:Flip ;Write a loading message

;Get the StartTime
StartTime = MilliSecs()

;Include pxGUI
Include "../pxGUI.bb"
;Because the API are in diferent diretory from your program, you need to set a path to the root of API
PX_SetRoot("../")

;Change background
PX_SetDefaultBackground()

;Load Font Images
PX_LoadFonts()

;Create 90 TextFields
For X = 0 To 4
	For Y = 1 To 18
		PX_Text(20+x*120, 8+25*Y, "JUST A TEST", 100, 20, "")
		Fields = Fields + 1
	Next
Next
;Calculate time taken to load
TimeToLoad = MilliSecs()-StartTime

;Main Loop
While Not KeyHit(1)
	
	;Clear screen
	Cls
	
	;Calculate FPS
	FramesPerSecond = 1000/(MilliSecs()-LastFrame)
	
	;Save when the last frame was rendered
	LastFrame = MilliSecs()
	
	PX_Write (070,0,"FPS: "+FramesPerSecond)           ;Write FPS
	PX_Write (170,0,"Num. of Fields: "+Fields)         ;Write number of text fields
	PX_Write (370,0,"Time to load: "+TimeToLoad+"ms")  ;Write time taken to load
	PX_Write (320,15,"Press spacebar to pause.",1,0,1) ;Write a Gray Static Message
	
	;Pause program
	If KeyHit(57) Then FlushKeys:WaitKey:FlushKeys
		
	;Update gadgets
	PX_Update()
	
	;Draw gadgets
	PX_Draw()
	
	;Switch buffers
	Flip
Wend
End