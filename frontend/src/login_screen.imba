import { apiClient } from './api_client'

tag register_form
	css d:flex fld:column
	css label fs:0.8em fw:bold c:cool4 pt:8px

	register = { username: '', password: '', email: '' }
	loading = false
	failed = false

	def do_register
		loading = true
		apiClient.post('/api/v1/users', register).then(do(res)
			if res.ok
				failed = false
				window.location.reload()
			else
				failed = true
		).catch(do
			failed = true
		).finally(do loading = false)

	<self>
		<h1 [fs:1.2em fw:normal m:0]> 'Create account'
		if failed
			<div.error-message> 'Registration failed! Please try again.'
		<label> 'Username:'
		<input type='text' bind=register.username>
		<label> 'Email:'
		<input type='text' bind=register.email>
		<label> 'Password:'
		<input type='password' bind=register.password>
		if loading
			'Loading...'
		else
			<button [mt:16px] @click=do_register> 'Register'

tag login-screen
	credentials = { username: '', password: '' }
	waiting = false
	failed = false
	show_register = false

	def do_login
		waiting = true
		const { username, password } = credentials
		credentials.password = ''
		apiClient.login(username, password)
			.then(do(result)
				waiting = false
				failed = !result
			).catch do
				waiting = false
				failed = true

	<self.border [maw:400px w:100% m:48px auto p:8px d:flex fld:column pos:relative]>
		css label fs:0.8em fw:bold c:cool4 pt:8px

		<div [w:fit-content m:8px auto fs:1.3em]> 'Student Calendar'
		if failed
			<div.error-message> 'Login failed! Please try again.'
		<label htmlFor='login-username'> 'Username:'
		<input#login-username type='text' bind=credentials.username @keydown.enter=do_login>
		<label htmlFor='login-password'> 'Password:'
		<input#login-password type='password' bind=credentials.password @keydown.enter=do_login>
		if waiting
			<div [m:16px auto 0]> 'Logging in...'
		else
			<button [mt:16px] @click=do_login> 'Login'
			<button.secondary [mt:16px] @click=(show_register = !show_register)> 'Register'

		if waiting
			<spinner>

		if show_register
			<register_form [mt:16px]>
