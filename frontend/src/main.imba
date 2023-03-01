import { apiClient } from './api_client'
import './login_screen'
import './calendar_screen'

global css @root ff:sans-serif c:cool0 bg:cool9
global css body m:0
global css .border bd:1px solid cool6 rd:8px
global css input bd:1px solid cool6 rd:4px bg:transparent c:current ff:sans fs:1em ol:none p:4px
	@focus bg:indigo4/10%
global css select bd:1px solid cool6 rd:4px bg:transparent c:current ff:sans fs:1em ol:none p:4px
	@focus bg:indigo4/10%
global css option c:cool9
global css button rd:4px bg:indigo6 bd:none ff:sans fs:1em fw:bold p:4px tween:background .2s cursor:pointer c:cool0
	@hover bg:indigo5
	@active bg:indigo7
global css button.secondary bd:1px solid cool6 bg:transparent bg@hover:cool0/10% bg@active:cool0/20%
global css button.delete bg:red7 bg@hover:red6 bg@active:red8
global css .error-message bg:red4/10% c:red0 bd:1px solid red4 p:4px rd:4px mt:8px


tag spinner
	css pos:absolute l:0 t:0 w:100% h:100% zi:90 bg:cool9/80% backdrop-filter:blur(1px)
	css .spinner w:32px h:32px rd:32px content:'' bd:2px solid transparent bct:cool0 transform:translate(-50%, -50%) rotate(0deg)
		pos:absolute l:50% t:50% animation:spin linear 1s infinite
	css @keyframes spin
		0% transform:translate(-50%, -50%) rotate(0deg)
		100% transform:translate(-50%, -50%) rotate(360deg)

	<self>
		<.spinner>


tag app
	<self [w:100%]>
		unless apiClient.token
			<login-screen>
		else
			<calendar-screen>

imba.mount <app>
