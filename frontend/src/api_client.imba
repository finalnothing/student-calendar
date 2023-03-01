class APIClient
	constructor(base_url)
		self.base_url = base_url
		self.token = null
		self.user = null

	def request(method, path, body = null)
		let headers = new Headers
		if token
			headers.append('Authorization', "Bearer {token}")
		if body
			body = JSON.stringify(body)
			headers.append('Content-Type', 'application/json')
		window.fetch(base_url + path, { headers, body, method })

	def get(path)
		request('GET', path)

	def post(path, body)
		request('POST', path, body)

	def put(path, body)
		request('PUT', path, body)

	def delete(path)
		request('DELETE', path)

	def login(username, password)
		post('/api/v1/auth/token', { username, password })
			.then(do(res)
				if res.ok
					self.token = (await res.text!)
					self.user = username
					return true
				return false
			).catch do(err)
				console.error("Login error: {err}")
				return false

	def logout
		self.token = null
		self.user = null


let port = window.location.port === '3000' ? '8080' : window.location.port
let url = "{window.location.protocol}//{window.location.hostname}:{port}"
export var apiClient = new APIClient(url)
