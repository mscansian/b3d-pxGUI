;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;             pxGUI                ;;
;;        By Matheus Cansian        ;;
;;       mscansian@gmail.com        ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;            HOW TO CREATE YOUR OWN FONTS              ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; To create your own fonts, use the Font Image Creator ;;
;; then rename the font to Font(NUMBER). Where (NUMBER) ;;
;; is the next number of existing fonts. And after all  ;;
;; just add a number in the Number_Of_Fonts% variable   ;;
;; in PX_LoadFonts()                                    ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;About
Const PX_Version$ = "BETA 1.0"

;Configuration
Const PX_Error_Msg$ = "Please review your code"
Const PX_Images_Dir$ = "GuiImages/"

;Create Global Variables
Global PX_Font1,PX_Font2
Global PX_Root$
Global Number_Of_Fonts

Dim PX_Fonts(0)

;GUI type definition
Type Gui
	Field Gadget%, ID%, Name$
	Field Value$
	Field Size%, Ysize%
	Field X%, Y%, realY%
	Field Enabled% = False
	Field Active%=False
	Field OtherConfig%  = 0
	Field OtherConfig2% = 0
	Field Anim%=0
	Field FDelay%=0
	Field Show%=1
	Field Image1%, Image2%, Image3%, Image4%
	Field Sound1% = 0
	Field Sound2% = 0
	Field Sound3% = 0
	Field Sound4% = 0
End Type

;;;;; Loading Functions ;;;;;

;void PX_LoadFonts( void )
;Load Fonts Images
Function PX_LoadFonts()
	Number_Of_Fonts% = 5
	Dim PX_Fonts(Number_Of_Fonts-1)
	
	For a = 0 To Number_Of_Fonts-1
		;Load all images
		PX_Fonts(a) = LoadAnimImage (PX_Root$+PX_Images_Dir$+"/Font"+Int(a+1)+".bmp",12,16,0,94)

		;Change Transparent Color
		MaskImage PX_Fonts(a),255,0,255

		;Check if images has been loaded
		If Not PX_Fonts(a) Then Return 0
	Next
	
	;Return TRUE if both images has been loaded
	Return True
End Function

;Free all font images and variables
Function PX_FreeFonts%()
	For a = 0 To Number_Of_Fonts-1
		FreeImage PX_Fonts(a):PX_Fonts(a) = 0
	Next
End Function

;Free ALL images in ALL objects (for use with PX_ReLoadImages())
;This function is usefull, when you need to free some space in memory, but you don't want to clear your gadgets
Function PX_FreeImages()
	For g.gui = Each gui
		If g\Image1 <> 0 Then FreeImage g\Image1:g\Image1 = 0
		If g\Image2 <> 0 Then FreeImage g\Image2:g\Image2 = 0
		If g\Image3 <> 0 Then FreeImage g\Image3:g\Image3 = 0
		If g\Image4 <> 0 Then FreeImage g\Image4:g\Image4 = 0
	Next
End Function

;Every image loads with the creation of a gadget, but if some reason (like using PX_FreeImages()) you need to Load it again
Function PX_ReLoadImages()
	PX_FreeImages()
	For g.gui = Each gui
		Select g\Gadget
		Case 1
			g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Field.bmp")
			g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnField.bmp")
			ResizeImage (g\Image1,g\size,g\ysize)
			ResizeImage (g\Image2,g\size,g\ysize)
		Case 2
			g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/CheckBox.bmp")
			g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/CheckBox2.bmp")
			g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnCheckBox.bmp")
			g\Image4 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnCheckBox2.bmp")
			ResizeImage (g\Image1,g\size,g\ysize)
			ResizeImage (g\Image2,g\size,g\ysize)
			ResizeImage (g\Image3,g\size,g\ysize)
			ResizeImage (g\Image4,g\size,g\ysize)
		Case 3
			g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Field.bmp")
			g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnField.bmp")
			ResizeImage (g\Image1,g\size,g\ysize)
			ResizeImage (g\Image2,g\size,g\ysize)
		Case 4
			g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Button.bmp")
			g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/Button2.bmp")
			g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnButton.bmp")
			ResizeImage (g\Image1,g\size,g\ysize)
			ResizeImage (g\Image2,g\size,g\ysize)
			ResizeImage (g\Image3,g\size,g\ysize)
		Case 5
			g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/ProgressBar.bmp")
			g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/ProgressBar2.bmp")
			g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnProgressBar.bmp")
			g\Image4 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnProgressBar2.bmp")
			ResizeImage (g\Image1,g\size,g\ysize)
			ResizeImage (g\Image3,g\size,g\ysize)
		End Select
	Next
End Function

;;;;; Gadget Delete Functions ;;;;;

;Delete a Single Gadget. Use it at your own risk
Function PX_KillGadget(Id%)
	G.gui = PX_GetHandle(id%)
	Delete G
End Function

;Clear all gadgets and free all gadgets images
Function PX_ClearGadgets()
	PX_FreeImages()
	For g.gui = Each gui
		Delete g
	Next
End Function

;;;;; Gadgets Create Functions ;;;;;

;Create a Textbox
Const PX_Letters = 1 ;(a, b, c, ...)
Const PX_Numbers = 2 ;(1, 2, 3, ...)
Const PX_Others  = 4 ;(!, @, #, ...)
Const PX_Space   = 8

;Sintax: int PX_Text (int X ,int Y ,string IntialValue[, int Width ,int Height ,string TopCaption ,int AllowedChars ,int MaxLenght])
;Sound1 = When the textfield is clicked (select)
;Sound2 = When return is pressed (unselect)
;Sound3 = Typing sound
;Sound4 = Backspace sound
Function PX_Text%(x%, y%, value$="", xsize%=200, ysize%=20, Caption$="", Char%=15, MaxChar%=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 1
	g\Name$   = Caption$
	g\value   = value$
	g\size    = xsize%
	g\ysize   = ysize%
	g\x       = x%
	g\y       = y%
	g\realy   = y% + (10 * (Len(Caption$) > 0))
	g\Active  = False
	g\enabled = True
	g\OtherConfig%  = Char%
	g\OtherConfig2% = MaxChar%
	g\Show = 1
	
	g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Field.bmp")
	g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnField.bmp")	
	
	ResizeImage (g\Image1,xsize%,ysize%)
	ResizeImage (g\Image2,xsize%,ysize%)
	;Return Gadget ID
	Return g\id
End Function

;Create a Password Box
;Sintax: int PX_Password (int X ,int Y ,string IntialValue[, int Width ,int Height ,string TopCaption ,Int AllowedChars ,int MaxLenght])
;Sound1 = When the textfield is clicked (select)
;Sound2 = When return is pressed (unselect)
;Sound3 = Typing sound
;Sound4 = Backspace sound
Function PX_Password%(x%,y%,value$="",xsize%=200,ysize%=20,Caption$="",Char%=15, MaxChar%=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 3
	g\Name$   = Caption$
	g\value   = value$
	g\size    = xsize%
	g\ysize   = ysize%
	g\x       = x%
	g\y       = y%
	g\realy   = y% + (10 * (Len(Caption$) > 0))
	g\Active  = False
	g\enabled = True
	g\OtherConfig% = Char%
	g\OtherConfig2% = MaxChar%	
	g\Show = 1
	
	g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Field.bmp")
	g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnField.bmp")	
	
	ResizeImage (g\Image1,xsize%,ysize%)
	ResizeImage (g\Image2,xsize%,ysize%)
	
	;Return Gadget ID	
	Return g\id
End Function

;Create a Image
;Sintax: int PX_Image (int X ,int Y ,string ImageFile [,bool CenterX ,bool CenterY ,byte MaskRed ,byte MaskGreen ,byte MaskBlue])
;No Sounds
Function PX_Image%(x%,y%,path$="",CenterX=0,CenterY=0,MaskR=0,MaskG=0,maskB=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 8
	g\value   = value$
	g\size    = CenterX
	g\ysize   = CenterY
	g\x       = x%
	g\y       = y%
	g\realy   = y%
	g\enabled = True
	g\Show = 1
	
	g\Image1  = LoadImage(path$)
	MaskImage g\Image1,MaskR,MaskG,MaskB

	;Return Gadget ID
	Return g\id
End Function

;Create a Label
;Sintax: int PX_Label (int X ,int Y ,string Text [,bool CenterX ,bool CenterY ,int Font])
;No Sounds
Function PX_Label%(x%,y%,value$="",CenterX=0,CenterY=0,FColor=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 7
	g\value   = value$
	g\size    = CenterX
	g\ysize   = CenterY
	g\x       = x%
	g\y       = y%
	g\realy   = y%
	g\Active  = FColor
	g\enabled = True
	g\Show = 1

	;Return Gadget ID
	Return g\id
End Function

;Create a Button
;Sintax: int PX_Button (int X, int Y [,string TopCaption ,int Width ,int Height ,bool Hidden])
;Sound1 = When you click the button
;Sound2 = When you release the button
Function PX_Button%(x%,y%,Caption$="Botão",xsize%=100,ysize%=30,Invisible%=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 4
	g\Name$   = Caption$
	g\value   = Invisible%
	g\size    = xsize%
	g\ysize   = ysize%
	g\x       = x%
	g\y       = y%
	g\realy   = y%
	g\Active  = False
	g\enabled = True
	g\Show = 1
	
	g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/Button.bmp")
	g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/Button2.bmp")	
	g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnButton.bmp")	
	
	ResizeImage (g\Image1,xsize%,ysize%)
	ResizeImage (g\Image2,xsize%,ysize%)
	ResizeImage (g\Image3,xsize%,ysize%)
	
	;Return Gadget ID	
	Return g\id
End Function

;Create a ProgessBar
;Sintax: int PX_ProgressBar (int X ,int Y ,int IntialValue [,int Width ,int Height ,string TopCaption])
;No Sounds
Function PX_ProgressBar%(x%,y%,Value%,xsize%=250,ysize%=20,Caption$="")
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 5
	g\Name$   = Caption$
	g\value   = Value%
	g\size    = xsize%
	g\ysize   = ysize%
	g\x       = x%
	g\y       = y%
	g\Active  = False
	g\Enabled = True
	g\Show = 1
	
	g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/ProgressBar.bmp")
	g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/ProgressBar2.bmp")	
	g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnProgressBar.bmp")
	g\Image4 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnProgressBar2.bmp")	
	
	ResizeImage (g\Image1,xsize%,ysize%)
	ResizeImage (g\Image3,xsize%,ysize%)
	
	;Return Gadget ID
	Return g\id
End Function

;Create a CheckBox
;Sintax: int PX_CheckBox (int X ,int Y ,string TopCaption [,bool Checked ,int Width ,int Height])
;Sound1 = When an option is checked
;Sound2 = When an option is unchecked
Function PX_CheckBox%(x%,y%,Caption$,Checked%=0,xsize%=15,ysize%=15)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 2
	g\Name$   = Caption$
	g\value   = Checked%
	g\size    = xsize%
	g\ysize   = ysize%
	g\x       = x%
	g\y       = y%
	g\realy   = y%
	g\enabled = True
	g\Show = 1
	
	g\Image1 = LoadImage(PX_Root$+PX_Images_Dir$+"/CheckBox.bmp")
	g\Image2 = LoadImage(PX_Root$+PX_Images_Dir$+"/CheckBox2.bmp")	
	g\Image3 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnCheckBox.bmp")
	g\Image4 = LoadImage(PX_Root$+PX_Images_Dir$+"/UnCheckBox2.bmp")	
	
	ResizeImage (g\Image1,xsize%,ysize%)
	ResizeImage (g\Image2,xsize%,ysize%)
	ResizeImage (g\Image3,xsize%,ysize%)
	ResizeImage (g\Image4,xsize%,ysize%)
	
	;Return Gadget ID	
	Return g\id
End Function

;Create a Credits Message
;Sintax: int PX_Credits (int X ,int Y [,int Font])
;No Sounds
Function PX_Credits%(x%,y%,FColor=0)
	g.gui     = New gui
	g\id      = PX_GetNextId%()
	g\Gadget  = 6
	g\Value$  = FColor
	g\x       = x%
	g\y       = y%
	g\realy   = y%
	g\Show = 1

	;Return Gadget ID	
	Return g\id
End Function

;;;;; Updating and Rendering Functions ;;;;;

;Update GUI clicks. Check if a gadget has been clicked, update values, etc...
;This function MUST run BEFORE PX_Draw()
Function PX_Update(RefreshMouse=1)
	AvoidErrors()
	K% = GetKey()
	If K
		For g.gui = Each gui
			If g\Active = True And (g\Gadget = 1 Or g\Gadget = 3)
				Select K
				Case 8
					If Len(g\value) > 0 Then g\value$ = Left(g\value,Len(g\value)-1)
					If g\Sound4 <> 0 Then PlaySound g\Sound4
				Case 13
					g\Active = False
					If g\Sound2 <> 0 Then PlaySound g\Sound2
				Default
					If CheckChar(K, g\OtherConfig)
						If (g\OtherConfig2 = 0) Or (g\OtherConfig2 > Len(g\value))
							If StringWidth(g\value + Chr(K)) <= g\size-5 Then g\value = g\value + Chr(K):If g\Sound3 <> 0 Then PlaySound g\Sound3
						EndIf
					EndIf
				End Select
			EndIf
			g\Fdelay = 0
		Next
	Else
		If KeyDown(14)
			For g.gui = Each gui
				If g\Active = True And (g\Gadget = 1 Or g\Gadget = 3)
					If g\Fdelay = 4
						If Len(g\value) > 0 Then g\value$ = Left(g\value,Len(g\value)-1)
						If g\Sound4 <> 0 Then PlaySound g\Sound4
					EndIf
					g\FDelay = G\Fdelay + 1
					If G\fdelay = 5 Then g\Fdelay = 0
				EndIf
			Next
		EndIf
	EndIf
	
	If Not MouseDown(1)
		For g.gui = Each gui
			If g\Gadget = 4 And g\anim = 1
				g\anim = 0
				If RectsOverlap(g\x,g\realy,g\size,g\ysize,MouseX(),MouseY(),1,1) Then g\Active = 1:If g\Sound2 <> 0 Then PlaySound g\Sound2
			EndIf
		Next
	EndIf

	For g.gui = Each gui
		If g\show = 0 Then Goto NextGui
		If g\enabled = True
			If RectsOverlap(g\x,g\realy,g\size,g\ysize,MouseX(),MouseY(),1,1)
				If MouseHit(1)
					ActBefore = g\Active
					PX_DesactivateAll()
					Select g\Gadget
					Case 1
						g\Active = True
						g\anim = 1
						If g\Sound1 <> 0 Then PlaySound g\Sound1
					Case 3
						g\Active = True				
						g\anim = 1
						If g\Sound1 <> 0 Then PlaySound g\Sound1
					Case 2
						g\value = Abs(Int(g\value)-1)
						If g\value = 1
							If g\Sound1 <> 0 Then PlaySound g\Sound1
						Else
							If g\Sound2 <> 0 Then PlaySound g\Sound2
						EndIf
					Case 4
						g\Anim = 1
						If g\Sound1 <> 0 Then PlaySound g\Sound1
					End Select
					Return
				EndIf
			EndIf
		EndIf
		.NextGui
	Next
	If RefreshMouse Then MouseHit(1)
End Function

;Draw GUI
Function PX_Draw(FlipBuffers%=0)
	PX_CurrentBuffer% = GraphicsBuffer()
	SetBuffer BackBuffer()
	For g.gui = Each gui
		If g\Show = 0 Then Goto NextGui
		Select g\Gadget
		Case 1
			If Len(g\Name$)>0
				PX_Write(g\x,g\y,g\Name$,0,1)
				c = 10
			EndIf
			If g\Enabled Then DrawImage g\Image1,g\x,g\y+c:Else:DrawImage g\Image2,g\x,g\y+c
			PX_Write(g\x+3,g\y+(g\ysize/2)+c,g\value,0,1,Abs(g\Enabled-1))
			If g\Active = True
				If g\Anim > 1 And g\Anim < 40 Then PX_Write(g\x+3+StringWidth(g\value),g\y+(g\ysize/2)+c-1,"|",0,1)
				g\Anim = g\Anim + 1
				If g\Anim = 80 Then g\Anim = 1
			EndIf
		Case 2
			If g\Enabled
				If g\value = 1 Then DrawImage g\Image1,g\x,g\y:Else:DrawImage g\Image2,g\x,g\y
			Else
				If g\value = 1 Then DrawImage g\Image3,g\x,g\y:Else:DrawImage g\Image4,g\x,g\y	
			EndIf
			PX_Write(g\x+g\size+7,g\y+(g\ysize/2),g\Name$,0,1,Abs(g\Enabled-1))
		Case 3
			If Len(g\Name$)>0
				PX_Write(g\x,g\y,g\Name$,0,1)
				c = 10
			EndIf
			If g\Enabled Then DrawImage g\Image1,g\x,g\y+c:Else:DrawImage g\Image2,g\x,g\y+c
			s$ = ""
			While Len(s$)<Len(g\value)
				s$ = s$ + "*"
			Wend
			PX_Write(g\x+3,g\y+(g\ysize/2)+c,s$,0,1,Abs(g\Enabled-1))
			If g\Active = True
				If g\Anim > 1 And g\Anim < 40 Then PX_Write(g\x+3+StringWidth(s$),g\y+(g\ysize/2)+c-1,"|",0,1)
				g\Anim = g\Anim + 1
				If g\Anim = 80 Then g\Anim = 1
			EndIf
		Case 4
			If g\value = 0
				If g\anim = False
					If g\Enabled Then DrawImage g\Image1,g\x,g\y:Else:DrawImage g\Image3,g\x,g\y
					PX_Write(g\x+(g\size/2),g\y+(g\ysize/2),g\Name$,1,1,Abs(g\Enabled-1))
				Else
					DrawImage g\Image2,g\x,g\y
					PX_Write(g\x+(g\size/2)+1,g\y+(g\ysize/2)+1,g\Name$,1,1)
				EndIf
			EndIf
		Case 5
			If Len(g\Name$)>0
				PX_Write(g\x,g\y,g\Name$,0,1)
				c = 10
			EndIf
			If g\Enabled Then DrawImage g\Image1,g\x,g\y+c:Else:DrawImage g\Image3,g\x,g\y+c
			Valor# = Float(g\value)
			If Floor(Valor#) > 100 Then Valor# = 100
			If Floor(Valor#) < 0 Then Valor# = 0
			Valor2# = Float(g\size-7)/100*Valor#
			For a = g\x+4 To Floor(g\x+3+Valor2)
				If g\Enabled Then DrawImage g\Image2,a,g\y+4+c:Else:DrawImage g\Image4,a,g\y+4+c
			Next
		Case 6
			PX_Write(g\x,g\y   ,"This interface was created with pxGUI",1,1,g\Value$)
			PX_Write(g\x,g\y+15,"Version: "+PX_Version$,1,1,g\Value$)
		Case 7
			PX_Write(g\x,g\y,g\Value,g\size,g\ysize,g\active)
		Case 8
			x = g\x
			y = g\y
			If g\size Then x = x - (ImageWidth(g\Image1)/2)
			If g\ysize Then y = y - (ImageHeight(g\Image1)/2)			
			DrawImage g\Image1,x,y
		Default
			PX_Error("Type "+g\Gadget+" dosen't exist")
		End Select
		.NextGui
	Next
	SetBuffer PX_CurrentBuffer%
	If FlipBuffers% Then Flip
End Function

;;;;; Control Functions ;;;;;

;Desactivate all gadgets
Function PX_DesactivateAll()
	For g.gui = Each gui
		If g\Gadget <> 7 Then g\Active = False
	Next
End Function

;Hide all gadgets
;Usefull when you need to Hide all gadgets but you need the values of them
Function PX_HideAll()
	For g.gui = Each gui
		g\Show = False
	Next
End Function

;Gets a name (ussualy the TopCaption) of a gadget
Function PX_GetName$(id%)
	g.gui = PX_Gethandle(id%)
	Return g\Name$
End Function

;Changes the name of a gadget
Function PX_SetName(id%,Name$)
	g.gui = PX_Gethandle(id%)
	g\Name$ = Name$
End Function

;Changes de value of a gadget
Function PX_SetValue(id%,Name$)
	g.gui = PX_Gethandle(id%)
	g\value$ = Name$
End Function

;Gets the value of a gadget
Function PX_GetValue$(id%)
	g.gui = PX_Gethandle(id%)
	Return g\value$
End Function

;Changes the state of a gadget
;1 = Enabled
;0 = Disabled
Function PX_SetState(id%,State%)
	g.gui = PX_Gethandle(id%)
	g\Enabled = State%
End Function

;Gets the state of a gadget
Function PX_GetState%(id%)
	g.gui = PX_Gethandle(id%)
	Return g\Enabled
End Function

;Turns gadget visiblity TRUE
Function PX_Show(id%)
	g.gui = PX_Gethandle(id%)
	g\Show = 1
End Function

;Turns gadget visiblity FALSE
Function PX_Hide(id%)
	g.gui = PX_Gethandle(id%)
	g\Show = 0
End Function

;Shows if gadget is visible
Function PX_GetVisibility%(id%)
	g.gui = PX_Gethandle(id%)
	Return g\Show
End Function

;Turns gadget visiblity TRUE or FALSE
Function PX_SetVisibility%(id%,vis%)
	g.gui = PX_Gethandle(id%)
	g\Show% = vis%
End Function

;Changes the position of a gadget
Function PX_SetPosition(id%,x%,y%)
	g.gui = PX_Gethandle(id%)
	g\x = x
	g\y = y
End Function

;Adds a value to a ProgressBar (in percentage)
Function PX_StepBar(id%,value#)
	If PX_GetState(id%) And PX_GetVisibility(id) Then PX_SetValue(id%,Float(PX_GetValue(id%))+value#)
End Function

;Something like KEYHIT
;Put the gadget handle in this function to check if the gadget has been clicked
;Or if you leave it blank, the function will return the handle of the lastest clicked gadget
;It just work with:
; - Buttons
; - CheckBoxes
Function PX_OnClick(Button%=0)
	If Button = 0
		For g.gui = Each gui
			If g\Show = 0 Then Goto NextGui
			If g\Gadget = 4 And g\Active = True Then g\Active = False:Return g\id
			.NextGui
		Next
	Else
		g = PX_GetHandle(Button%)
		If G\Gadget = 4
			If G\Active = True And g\Show = 1 Then g\Active = False:Return g\id
		ElseIf G\Gadget = 2
			If G\Value <> g\OtherConfig% Then g\OtherConfig% = Int(G\value):Return g\id
		Else
			PX_Error("PX_OnClick(): Gadget "+Button%+" is not suported by this function")
		EndIf
		Return False
	EndIf
End Function

;Gets an object handle by pxGUI handle (usually called ID)
Function PX_GetHandle.gui(Id%)
	For g.gui = Each gui
		If g\id = id% Then Return g.gui
	Next
	PX_Error("Gadget "+Id%+" dosen't exists")
End Function

;Gets the number of the next ID
Function PX_GetNextId%()
	For g.gui = Each gui
		c = c + 1
	Next
	Return c+1
End Function

;Set gadgets Sounds
;Reference of each sound is shown in the gadget function description
Function PX_SetSounds(id%, PX_Sound1%, PX_Sound2%=0, PX_Sound3%=0, PX_Sound4%=0)
	g.gui = PX_Gethandle(id%)
	g\Sound1% = PX_Sound1%
	g\Sound2% = PX_Sound2%
	g\Sound3% = PX_Sound3%
	g\Sound4% = PX_Sound4%		
End Function

;;;;; Other Functions ;;;;;

;Sets the backgorund color to 175, 175, 175 (Gray)
Function PX_SetDefaultBackGround(Clear%=1)
	ClsColor 175,175,175
	If Clear Then Cls
End Function

;Writes with Font images, which is faster
;If you want to add more fonts, look at PX_LoadFonts() or create a Temporary Font with PX_NewTemporaryFont()
;Sintax: void PX_Write (int X ,int Y ,string Text ,bool CenterX ,bool CenterY ,int Font)
Function PX_Write(X%,Y%,Texto$,CenterX%=0,CenterY%=0,FColor=0)
	If PX_Fonts(a) = 0 Then PX_LoadFonts()
	Cell = 8
	If FColor < Number_Of_Fonts%
		PX_SelFont = PX_Fonts(FColor)
	Else
		PX_SelFont = FColor
	EndIf
	
	If CenterX Then X% = X% - (Len(Texto)*Cell/2)
	If CenterY Then Y% = Y% - 8
	For A = 1 To Len(Texto$)
		ACSII% = Asc(Right(Left(Texto$,a),1))
		If ACSII = 32
			DrawImage PX_SelFont,X%+(a-1)*Cell,Y%,93
		ElseIf ACSII-33 > 93
			PX_Error("Characterer "+Chr(ACSII)+" ("+ACSII+") is Not suportet by this font")
		Else
			DrawImage PX_SelFont,X%+(a-1)*Cell,Y%,ACSII-33
		EndIf
	Next
End Function

;1 - Letters
;2 - Numbers
;4 - Other (!, @, #, ...)
;8 - Space
;Check if a char match with the given patern
;This function has no use for you :)
Function CheckChar(K, O)
	If K = 32 And O => 8 Then Return True ;Return Space
	If K = 32 And O < 8  Then Return False
	If O => 8 Then O = O - 8 ;Remove Space

	If  K => 32 And K =< 126 And O = 7 Then Return True ;All
	If (K => 65 And K =< 90 Or K => 97 And K =< 122) And O = 1 Then Return True ;Letters
	If (K => 48 And K =< 57) And O = 2 Then Return True ;Numbers
	If (K => 58 And K =< 64 Or K => 94 And K =< 96 Or K => 123 And K =< 126) And O = 4 Then Return True ;Other
	
	If ((K => 65 And K =< 90 Or K => 97 And K =< 122) Or (K => 48 And K =< 57)) And O = 3 Then Return True ;Letter + Number
	If ((K => 65 And K =< 90 Or K => 97 And K =< 122) Or (K => 58 And K =< 64 Or K => 94 And K =< 96 Or K => 123 And K =< 126)) And  O = 5 Then Return True ;Leter + Other
	If ((K => 48 And K =< 57) Or (K => 58 And K =< 64 Or K => 94 And K =< 96 Or K => 123 And K =< 126)) And O = 6 Then Return True ;Number + Other
End Function


Function PX_NewTemporaryFont% (px_r%, px_g%, px_b%)
	;Create the Image
	PX_Imagem% = CreateImage(1128,16)
	PX_Buffer% = GraphicsBuffer()
	SetBuffer ImageBuffer(PX_Imagem%)
	
	;Draw Transparent Color
	Color 255,0,255
	Rect 0,0,1128,16,1

	;Avoid Transparency Errors
	If px_r% = 255 And px_g% = 0 And px_b% = 255 Then px_r% = 254

	;Change font color
	Color px_r%, px_g%, px_b%

	;Draw all Chars
	For PX_Char% = 33 To 125
		Text (PX_Char%-33)*12,1,Chr(PX_Char%)
	Next

	;Save the Font
	PX_FontName% = MilliSecs()
	SaveBuffer (ImageBuffer(PX_Imagem%),PX_Root$+PX_Images_Dir$+"/~"+PX_FontName%+".bmp")
	PX_FontFile% = LoadAnimImage (PX_Root$+PX_Images_Dir$+"/~"+PX_FontName%+".bmp",12,16,0,94)
	MaskImage PX_FontFile%,255,0,255
	DeleteFile PX_Root$+PX_Images_Dir$+"/~"+PX_FontName%+".bmp"
	
	SetBuffer PX_Buffer%
	Return PX_FontFile%
End Function

Function PX_FreeTemporaryFont (px_id%)
	FreeImage px_id%
End Function

;;;;;;;;;;;;;;;;; Debug/Other Functions ;;;;;;;;;;;;;;;;;;;;;

;If you are using the API in other directory of your program, you need to set the path from your program to the API
Function PX_SetRoot(root$)
	PX_Root$ = root$
End Function

;Shows an error message
;No use for you :)
Function PX_Error(Message$)
	RuntimeError("Error! "+Message$+". "+PX_Error_Msg$)
End Function

;This function automatically run in every PX_Update()
;It's here to avoid errors made by YOU! :)
;This function can be disabled for performance improvement
Function AvoidErrors()
	For g.gui = Each gui
		If g\Enabled > 1 Then g\Enabled = 1
		If g\Enabled < 1 Then g\Enabled = 0
	Next
End Function

;;;;; Not Related with GUI Functions ;;;;;

;; PX Timing ;;

;This functions are here if you want to make an easy FPS control
;They are not part of the GUI

;Set Global Variable
Global PX_FPS_M

;Start the counter
;Put in the start of your loop
Function PX_TimingStart()
	PX_FPS_M = MilliSecs()
End Function

;This function will calculate the delay to the game keep always the same FPS
;Put in the end of your loop
;Sintax: void PX_TimingEnd ([int MaxFPS])
Function PX_TimingEnd(PX_Time%=50)
	PX_D% = PX_FPS_M-MilliSecs()
	PX_D% = PX_Time%-PX_FPS_M
	If PX_D% > 0 Then Delay PX_D%
End Function

;;;;; END OF PXGUI CODE ;;;;;