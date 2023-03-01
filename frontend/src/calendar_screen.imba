import { icons } from 'feather-icons'
import { apiClient } from './api_client'

def add_days(date, days)
	let new_date = new Date(date)
	new_date.setDate(new_date.getDate() + days)
	return new_date

tag icon
	attr icon
	attr size = 24px
	<self [w:{size} h:{size} d:inline-block] innerHTML=icons[icon].toSvg({ width: size, height: size, 'stroke-width': 2 })>

tag btn
	attr icon
	attr size = 24px
	css d:inline-flex bg:transparent ai:center cursor:pointer rd:4px p:4px box-sizing:border-box m:4px us:none
		@hover bg:cool0/20%
		@active bg:cool0/40%
	css self.primary bg:indigo6
		@hover bg:indigo5
		@active bg:indigo7
	css &.secondary bd:1px solid cool0/40%
	<self>
		<icon icon=icon size=size>
		<slot>



tag topbar
	css w:100% h:48px bg:cool7 d:flex p:4px box-sizing:border-box pos:fixed t:0 zi:100
	css .elevate bg:cool5 rd:4px p:4px

	def do_logout
		apiClient.logout()
		window.location.reload()

	def do_advance(days)
		data.weekOf = add_days(data.weekOf, days)
		emit('calendarchanged')

	<self>
		<div.elevate [d:flex ai:center mr:8px pl:12px]>
			<div [d:flex fld:column fs:.8em fw:bold us:none]>
				<div> data.weekOf.toLocaleDateString()
				<div> add_days(data.weekOf, 6).toLocaleDateString()
			<btn [bg:transparent] icon='chevron-left' @click=do_advance(-7)>
			<btn [bg:transparent] icon='chevron-right' @click=do_advance(7)>
		<div [d:flex ai:center]>
			<btn.secondary icon='plus' route-to='/courses/new'> 'Course'
			<btn icon='list' route-to='/courses'>
		<div [flg:1]>
		<div.elevate [cursor:pointer us:none bg@hover:cool4] @click=do_logout>
			<div> apiClient.user
			<div [fs:.7em fw:bold]> 'LOGOUT'


tag time_column
	<self>
		<div [h=32px]>
		for hour in [0 .. 23]
			<div [h:50px ta:right pr:4px us:none]> "{hour}:00"

tag day_column
	attr name
	attr day
	css bdl:1px solid cool6

	timeRegex = /^([0-9]{2}):([0-9]{2}):([0-9]{2})$/

	def filter_events(events)
		return events.filter do(event)
			return (new Date(event.occursOn)).getDay() === parseInt(day)

	def timeToPixel(str)
		let md = timeRegex.exec(str)
		return (parseInt(md[1]) + (parseInt(md[2]) / 60)) * 50

	<self [pos:relative]>
		<div [fs:1.4em pos:fixed pl:4px us:none bg:cool9/50% w:100% zi:50 backdrop-filter:blur(10px)]> name
		<div [h:32px]>
		for hour in [0 .. 23]
			<div [h:50px bdb:1px solid cool6/20% box-sizing:border-box]>

		for event in filter_events(data.events)
			let from = timeToPixel(event.fromTime) + 32
			let to = timeToPixel(event.toTime) + 32
			<div [pos:absolute t:{from}px h:{to - from}px bg:{event.course.color} w:100% p:4px box-sizing:border-box rd:4px us:none cursor:pointer]
				route-to="/events/{event.id}">
				<div [fw:bold d:flex ai:center]>
					<div [flg:1]> event.name
					if event.tasks.length > 0
						<icon [ml:4px] size=1em icon='clipboard'>
					<icon size=1em icon='edit' route-to="/events/{event.id}/edit">
				<div [fw:bold fs:.7em]>
					if event.name !== event.course.name
						"{event.course.name} / "
					event.location
					"(#{event.id})"

tag create_course
	css w:100% <600px bg:cool7/70% pos:fixed l:50% t:50% transform:translate(-50%, -50%) backdrop-filter:blur(5px)
		rd:8px bxs:0 0 12px black/40% d:flex fld:column
	css h1 m:0 0 8px fs:1.2em p:8px fw:normal d:flex ai:center jc:space-between
	css label m:8px 8px 4px 10px fs:.8em fw:bold o:80%
	css input,select m:0 8px
	css button m:12px 8px 8px

	course = { name: '', weekday: 'mon', location: '' }
	waiting = false
	failed = false

	def do_create
		waiting = true
		apiClient.post('/api/v1/courses', course).then(do(res)
			if res.ok
				emit('calendarchanged')
				course = { name: '', weekday: 'mon', location: '' }
				failed = false
				router.go('/')
			else
				failed = true
			waiting = false
		).catch(do
			waiting = false
			failed = true
		)

	<self>
		<h1> 'Create Course'
			<btn icon='x' route-to='/'>
		if failed
			<%errormessage [m:8px bg:red4/10% c:red0 bd:1px solid red4 p:4px rd:4px mt:8px]>
				'Creation failed. Please check your input and try again.'
		<label htmlFor='create_course-name'> 'Name:'
		<input#create_course-name bind=course.name type='text'>
		<label htmlFor='create_course-color'> 'Color:'
		<input#create_course-color bind=course.color type='color'>
		<label htmlFor='create_course-weekday'> 'Weekday:'
		<select#create_course-weekday bind=course.weekday value='mon'>
			<option value='mon'> 'Monday'
			<option value='tue'> 'Tuesday'
			<option value='wed'> 'Wednesday'
			<option value='thu'> 'Thursday'
			<option value='fri'> 'Friday'
		<label htmlFor='create_course-from_time'> 'Time:'
		<div [d:flex jc:stretch ai:center]>
			<input#create_course-from_time [flg:1] bind=course.fromTime type='time'>
			'−'
			<input [flg:1] type='time' bind=course.toTime>
		<label htmlFor='create_course-valid_from'> 'Valid:'
		<div [d:flex jc:stretch ai:center]>
			<input#create_course-valid_from [flg:1] bind=course.validFrom type='date'>
			'−'
			<input [flg:1] type='date' bind=course.validUntil>
		<label htmlFor='create_course-name'> 'Location:'
		<input#create_course-name bind=course.location type='text'>
		<button @click=do_create> 'Create'
		if waiting
			<spinner>

tag courses_list
	css w:100% <400px bg:cool7/70% pos:fixed l:50% t:50% transform:translate(-50%, -50%) backdrop-filter:blur(5px)
		rd:8px bxs:0 0 12px black/40% d:flex fld:column of:hidden
	css h1 m:0 0 8px fs:1.2em p:8px fw:normal d:flex ai:center jc:space-between
	css label m:8px 8px 4px 10px fs:.8em fw:bold o:80%
	css input,select m:0 8px
	css button m:12px 8px 8px

	courses = []

	def reload_courses
		courses = await apiClient.get('/api/v1/courses').then(do(res) res.json())

	def mount
		await reload_courses()

	def delete_course(id)
		apiClient.delete("/api/v1/courses/{id}").then(do(res)
			if res.ok
				emit('calendarchanged')
		)

	<self @calendarchanged=reload_courses>
		<h1> 'Courses'
			<btn icon='x' route-to='/'>
		for course in courses
			<div [p:8px d:flex ai:center bg@hover:cool0/10% tween:background .1s]>
				<div [w:16px h:16px bg:{course.color} rd:16px mr:8px bd:1px solid cool0]>
				course.name
				<div [flg:1]>
				<btn icon='edit' size=16px route-to="/courses/{course.id}">
				<btn icon='trash' size=16px @click=delete_course(course.id)>
		if courses.length === 0
			<div [p:8px m:0 auto 8px c:cool4]> 'You have no courses, yet.'

tag edit_course
	css w:100% <400px bg:cool7/70% pos:fixed l:50% t:50% transform:translate(-50%, -50%) backdrop-filter:blur(5px)
		rd:8px bxs:0 0 12px black/40% d:flex fld:column of:hidden
	css h1 m:0 0 8px fs:1.2em p:8px fw:normal d:flex ai:center jc:space-between
	css label m:8px 8px 4px 10px fs:.8em fw:bold o:80%
	css input,select m:0 8px
	css button m:12px 8px 8px

	waiting = false
	result = null
	course = {}

	def mount
		apiClient.get("/api/v1/courses/{route.params.id}").then(do(res)
			if res.ok
				course = await res.json()
			else
				router.go('/')
		).catch(do router.go('/'))

	def do_save
		waiting = true
		result = null
		apiClient.put("/api/v1/courses/{course.id}", course).then(do(res)
			if res.ok
				emit('calendarchanged')
				result = 'success'
			else
				result = 'failed'
			waiting = false
		).catch(do
			waiting = false
			result = 'failed'
		)

	<self>
		<h1> 'Edit Course'
			<btn icon='x' route-to='/'>
		if result === 'failed'
			<%errormessage [m:8px bg:red4/10% c:red0 bd:1px solid red4 p:4px rd:4px mt:8px]>
				'Action failed. Please check your input and try again.'
		elif result === 'success'
			<%errormessage [m:8px bg:green4/10% c:green0 bd:1px solid green4 p:4px rd:4px mt:8px]>
				'Saved succesfully.'
		<label> 'Name:'
		<input bind=course.name type='text'>
		<label> 'Color:'
		<input bind=course.color type='color'>
		<label> 'Weekday:'
		<select bind=course.weekday value='mon'>
			<option value='mon'> 'Monday'
			<option value='tue'> 'Tuesday'
			<option value='wed'> 'Wednesday'
			<option value='thu'> 'Thursday'
			<option value='fri'> 'Friday'
		<label> 'Time:'
		<div [d:flex jc:stretch ai:center]>
			<input [flg:1] bind=course.fromTime type='time'>
			'−'
			<input [flg:1] type='time' bind=course.toTime>
		<label> 'Valid:'
		<div [d:flex jc:stretch ai:center]>
			<input [flg:1] bind=course.validFrom type='date'>
			'−'
			<input [flg:1] type='date' bind=course.validUntil>
		<label> 'Location:'
		<input bind=course.location type='text'>
		<button @click=do_save> 'Save'
		if waiting
			<spinner>

tag edit_event
	css w:100% <400px bg:cool7/70% pos:fixed l:50% t:50% transform:translate(-50%, -50%) backdrop-filter:blur(5px)
		rd:8px bxs:0 0 12px black/40% d:flex fld:column of:hidden
	css h1 m:0 0 8px fs:1.2em p:8px fw:normal d:flex ai:center jc:space-between
	css label m:8px 8px 4px 10px fs:.8em fw:bold o:80%
	css input,select m:0 8px
	css button m:12px 8px 8px

	waiting = false
	result = null
	event = {}

	def update_event
		apiClient.get("/api/v1/events/{route.params.id}").then(do(res)
			if res.ok
				event = await res.json()
			else
				router.go('/')
		).catch(do router.go('/'))

	def mount
		update_event()

	def do_save
		waiting = true
		result = null
		apiClient.put("/api/v1/events/{event.id}", event).then(do(res)
			if res.ok
				emit('calendarchanged')
				result = 'success'
			else
				result = 'failed'
			waiting = false
		).catch(do
			waiting = false
			result = 'failed'
		)

	def do_reset
		waiting = true
		result = null
		apiClient.delete("/api/v1/events/{event.id}").then(do(res)
			if res.ok
				emit('calendarchanged')
				result = 'success'
				update_event()
			else
				result = 'failed'
			waiting = false
		).catch(do
			waiting = false
			result = 'failed'
		)

	<self>
		<h1> 'Edit Event'
			<btn icon='x' route-to='/'>
		if result === 'failed'
			<%errormessage [m:8px bg:red4/10% c:red0 bd:1px solid red4 p:4px rd:4px mt:8px]>
				'Action failed. Please check your input and try again.'
		elif result === 'success'
			<%errormessage [m:8px bg:green4/10% c:green0 bd:1px solid green4 p:4px rd:4px mt:8px]>
				'Saved succesfully.'
		<label> 'Occurs on:'
		<input type='date' bind=event.occursOn>
		<label> 'Name:'
		<input type='text' bind=event.name>
		<label> 'Time:'
		<div [d:flex jc:stretch ai:center]>
			<input [flg:1] type='time' bind=event.fromTime>
			'−'
			<input [flg:1] type='time' bind=event.toTime>
		<label> 'Location:'
		<input type='text' bind=event.location>
		<button @click=do_save> 'Save'
		<button.delete @click=do_reset> 'Reset'
		if waiting
			<spinner>

tag event_details
	css w:100% <400px bg:cool7/70% pos:fixed l:50% t:50% transform:translate(-50%, -50%) backdrop-filter:blur(5px)
		rd:8px bxs:0 0 12px black/40% d:flex fld:column of:hidden
	css h1 m:0 0 8px fs:1.2em p:8px fw:normal d:flex ai:center jc:space-between
	css h2 m:4px 0 fs:1em p:8px fw:bold d:flex ai:center jc:space-between

	event = { tasks: [] }
	new_task = { title: '' }
	error = ''

	def update_event
		apiClient.get("/api/v1/events/{route.params.id}").then(do(res)
			if res.ok
				event = await res.json()
			else
				router.go('/')
		).catch(do router.go('/'))

	def mount
		update_event()

	def add_task
		apiClient.post("/api/v1/events/{event.id}/tasks", new_task).then(do(res)
			if res.ok
				new_task.title = ''
				update_event()
			else
				error = 'Could not add task.'
		).catch do error = 'Could not add task.'

	def toggle_task(task)
		task.completed = !task.completed
		apiClient.put("/api/v1/tasks/{task.id}", task).then(do(res)
			if res.ok
				update_event()
			else
				error = 'Could not update task.'
		).catch do error = 'Could not update task.'

	def delete_task(task)
		apiClient.delete("/api/v1/tasks/{task.id}").then(do(res)
			if res.ok
				update_event()
			else
				error = 'Could not delete task.'
		).catch do error = 'Could not delete task.'


	<self>
		<h1> event.name
			<btn icon='x' route-to='/'>
		if error.length > 0
			<%errormessage [m:8px bg:red4/10% c:red0 bd:1px solid red4 p:4px rd:4px mt:8px]>
				error
		<h2> 'Tasks'
		for task in event.tasks.sort(do(a, b) a.id >= b.id ? 1 : -1 )
			<div [p:8px d:flex ai:center]>
				<icon [mr:8px cursor:pointer] size=1em icon=(task.completed ? 'check-square' : 'square') @click=toggle_task(task)>
				<div [flg:1]> task.title
				<icon [cursor:pointer] size=1em icon='trash' @click=delete_task(task)>
		<div [p:8px d:flex ai:center bdt:1px solid cool0/40%]>
			<icon size=1em icon='plus'>
			<input [bd:none bg:transparent flg:1] type='text' bind=new_task.title placeholder='Add task' @keydown.enter=add_task>

tag calendar
	css d:grid gtc:100px repeat(5, 1fr)

	<self>
		<time_column>
		<day_column name='mon' bind=data day=1>
		<day_column name='tue' bind=data day=2>
		<day_column name='wed' bind=data day=3>
		<day_column name='thu' bind=data day=4>
		<day_column name='fri' bind=data day=5>

		<create_course route='/courses/new$'>
		<courses_list route='/courses$'>
		<edit_course route='/courses/:id$'>
		<edit_event route='/events/:id/edit$'>
		<event_details route='/events/:id'>


tag calendar-screen
	state = {
		weekOf: get_monday(new Date()), # monday of the displayed week
		events: []
	}

	def update_events
		from = state.weekOf.toISOString().substring(0, 10);
		to = add_days(state.weekOf, 6).toISOString().substring(0, 10);
		apiClient.get("/api/v1/events?from={from}&to={to}").then do(res)
			if res.ok
				state.events = await res.json()
				imba.commit()

	def mount
		update_events()

	def get_monday(date)
		d = new Date(date)
		day = d.getDay()
		diff = d.getDate() - day + (day == 0 ? -6 : 1)
		return new Date(d.setDate(diff))

	<self @calendarchanged=update_events>
		<div [h:48px]> # placeholder for fixed <topbar>
		<topbar bind=state>
		<calendar bind=state>
