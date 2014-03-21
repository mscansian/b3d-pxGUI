Const UseOddScreen = 1 ;Change to 0 if you are having MAV errors

;Start Graphics
If UseOddScreen Then Graphics 300,280,16,2:Else:Graphics 640,480,16,2
AppTitle "pxGUI - Example 2"
SetBuffer BackBuffer()
Text 150,115,"Loading pxGUI, please wait.",1,1:Flip ;Write a loading message

;Include pxGUI
Include "../pxGUI.bb"
;Because the API are in diferent diretory from your program, you need to set a path to the root of API
PX_SetRoot("../")

;Change background
PX_SetDefaultBackground()

;Load Font Images
PX_LoadFonts()

;Create Main Gadgets
Label1     = PX_Label (150,15,"Welcome to Invader",1,0)
Name%      = PX_Text(10,45,"",280,20,"Nickname:")
Play%      = PX_Button(100,100,"Play",100,30)
HiScores%  = PX_Button(100,140,"Highscores",100,30)
Quit%      = PX_Button(100,180,"Quit",100,30)

			 ;Just put 2 credits lines (You can remove this)
			 PX_Credits(150,250)

;Create HighScores Gadgets
Dim HighScore(10) ;Var to save all scores
;Create 10 scores
For y = 1 To 10
HighScore(y) = PX_Label (150,25+y*15,y+" - Pexe da Silva",1,0) ;Create a label (10 times)
PX_Hide(HighScore(y)) ;Hide this gadget
Next
HiExit = PX_Button(100,210,"Back",100,25) ;Create an exit button
PX_Hide(HiExit) ;Hide it

;Create Play Gadgets
Bar1 = PX_ProgressBar(20,180,0,260,20,"Loading...") ;Create a progress bar
PX_Hide(Bar1); Hide it
Img1 = PX_Image(150,50,"Asteroids.JPG",1,0,255,0,0) ;Load an image
PX_Hide(Img1);Hide it

While Not KeyHit(2)
	
	;Clear screen
	Cls
	
	;If bar is enabled, update it
	PX_StepBar(Bar1,0.5)
	
	;If field Name is not empty, enable play button
	PX_SetState(Play%,Len(PX_GetValue(Name%)))
	
	;If ProgressBar reaches 100%
	If Floor(PX_GetValue(Bar1)) => 100
		;If Bar is visible
		If PX_GetVisibility(Bar1) = 1
			;Hide bar
			PX_Hide(Bar1)
			
			;Create an error label
			PX_Label(150,170,"Loading Error! (this is proposital)",1,0,1)
			
			;Create an exit button
			ExitButton = PX_Button(100,195,"Exit",100,20)
			
		;If bar is hidden
		Else
			;If Exit button was clicked then end
			If PX_OnClick(ExitButton) Then End
		EndIf
	EndIf
	
	;Get last button click
	Select PX_OnClick()
	;If Play button was clicked
	Case Play%
		;Hide all main menu
		PX_Hide(Name)
		PX_Hide(Play)
		PX_Hide(Hiscores)
		PX_Hide(Quit)
		
		;Show Bar and Image
		PX_Show(Bar1)
		PX_Show(Img1)
		
	;If Hiscore button was clicked
	Case HiScores%
		;Hide all main menu (exept the top label)
		PX_Hide(Name)
		PX_Hide(Play)
		PX_Hide(Hiscores)
		PX_Hide(Quit)
		
		;Show exit button
		PX_Show(HiExit)
		;Show all HiScore Labels
		For y = 1 To 10:PX_Show(HighScore(y)):Next
		;Change Top Label Value
		PX_SetValue(Label1,"HighScores")
	;If Quit button was cliked
	Case Quit
		;Terminate this program
		End
	;If "Back" btton was clicked
	Case HiExit
		;Show all main menu
		PX_Show(Name)
		PX_Show(Play)
		PX_Show(Hiscores)
		PX_Show(Quit)
		
		;Hide all HiScore gadgets
		PX_Hide(HiExit)
		For y = 1 To 10:PX_Hide(HighScore(y)):Next
		;Change the value of Top Label
		PX_SetValue(Label1,"Welcome to Invader")
	End Select
	
	;Update and Draw gadgets
	PX_Update()
	PX_Draw()
	
	;Switch Buffer
	Flip
Wend
End