;Start Graphics
Graphics 320,240,16,2
AppTitle "Font Image Creator"

SetBuffer BackBuffer()
Text 160,120,"Loading pxGUI, please wait.",1,1:Flip ;Write a loading message

;Include pxGUI
Include "../../pxGUI.bb"
;Because the API are in diferent diretory from your program, you need to set a path to the root of API
PX_SetRoot("../../")

;Set Background color to 175,175,175, and Clear the screen
PX_SetDefaultBackGround(True)

;Load Fonts
PX_LoadFonts()

;Create Gadgets
Name%       = PX_Text  (35 ,10 ,"MyFont",250,20,"Name of your font:",11)
RedColor%   = PX_Text  (35 ,50 ,"255",250,20,"Red color (RGB):",2,3)
GreenColor% = PX_Text  (35 ,90 ,"255",250,20,"Green color (RGB):",2,3)
BlueColor%  = PX_Text  (35 ,130,"255",250,20,"Blue color (RGB):",2,3)
Button%     = PX_Button(35 ,200,"Create Font Image",250)
Fonte% = LoadFont ("Tahoma",20,1)
F2% = LoadFont("blitz")

While Not KeyHit(1)
	;If button is hit
	If PX_OnClick(Button) Then Continue=1:Exit

	;Set status of the create button
	PX_SetState(Button,(Len(PX_GetValue(Name%)) > 0))
	;Update GUI
	PX_Update()
	
	;Clear the screen
	Cls
	;Draw GUI
	PX_Draw()

	;Draw Text
	Color PX_GetValue(RedColor%), PX_GetValue(GreenColor%), PX_GetValue(BlueColor%)
	SetFont Fonte%
	Text 160,170, "Your font will look like this!",1,0
	SetFont F2%
	;Switch buffers
	Flip
Wend

If Continue=0 Then End

;Delete all gadgets
PX_HideAll()
PX_FreeImages()
Cls

;Create New Gadgets
Label%  = PX_Label(160,15 ,"Your font is being created",1,0)
Label2% = PX_Label(160,35 ,"Please wait...",1,0,1)
Label3% = PX_Label(160,140,"Your font has been created!",1,0)
Label4% = PX_Label(160,160,"Press any key to exit...",1,0)
ProgressBar% = PX_ProgressBar(35,80,0)

;Hide 2 labels
PX_Hide(Label3%)
PX_Hide(Label4%)

;Draw the gadgets, fliping the screen
PX_Draw(1)

;Create the Image
Imagem% = CreateImage(1128,16)
SetBuffer ImageBuffer(Imagem%)
PX_StepBar(ProgressBar%,25):PX_Draw(1)

;Draw Transparent Color
Color 255,0,255
Rect 0,0,1128,16,1
PX_StepBar(ProgressBar%,25):PX_Draw(1)

;Avoid Transparency Errors
If PX_GetValue(RedColor%) = 255 And PX_GetValue(GreenColor%) = 0 And PX_GetValue(BlueColor%) = 255 Then PX_SetValue(RedColor%,254)

;Change font color
Color PX_GetValue(RedColor%), PX_GetValue(GreenColor%), PX_GetValue(BlueColor%)

;Draw all Chars
For Char% = 33 To 125
	Text (Char%-33)*12,1,Chr(Char%)
Next
PX_StepBar(ProgressBar%,25):PX_Draw(1)

;Save the Font
SaveBuffer (ImageBuffer(Imagem%),PX_GetValue(Name%)+".bmp")
PX_StepBar(ProgressBar%,25):PX_Draw(1)

PX_Show(Label3%)
PX_Show(Label4%)
PX_Draw(1)
FlushKeys:WaitKey
End