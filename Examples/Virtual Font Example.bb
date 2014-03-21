;Start Graphics
Graphics 640,480,16,2
AppTitle "pxGUI - Temporary Fonts Example"
SetBuffer BackBuffer()
Text GraphicsWidth()/2,215,"Loading pxGUI, please wait.",1,1:Flip:Cls ;Write a loading message

;Include pxGUI
Include "../pxGUI.bb"
;Because the API are in diferent diretory from your program, you need to set a path to the root of API
PX_SetRoot("../")

PX_LoadFonts()

Text GraphicsWidth()/2,215,"Creating fonts, please wait.",1,1:Flip ;Write a  font loading message
Timer = MilliSecs() ;Timer Start

;Create temporary fonts
Yellow_Font = PX_NewTemporaryFont(255,255,0)
Blue_Font   = PX_NewTemporaryFont(0,0,255)

Timer = MilliSecs()-Timer ;Calculate loading time

;Set Background color to 175,175,175, and Clear the screen
PX_SetDefaultBackGround(True)

While Not KeyHit(1)
		
	;Clear the screen
	Cls
	
	;Write text
	PX_Write(GraphicsWidth()/2, GraphicsHeight()/2,    "This font was created during the program loading",1,1, Yellow_Font)
	PX_Write(GraphicsWidth()/2, GraphicsHeight()/2+20, "It tooks "+Timer+" milliseconds to be created",1,1, Blue_Font)	
		
	;Switch buffers
	Flip	
Wend
;Free fonts (not a rule, but it's a good pratice)
PX_FreeTemporaryFont(Yellow_Font)
PX_FreeTemporaryFont(Blue_Font)
End