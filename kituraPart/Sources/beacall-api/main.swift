import Kitura

let router = Router()

let user: [[String:String]]

user = [["name":"student1","password":"test"],["name":"student2","password":"test2"]]

router.all("/", middleware: BodyParser(), StaticFileServer(path: "./Public"))

router.get("/connect/:name/:password") { request, response, next in
    let studentName = request.parameters["name"] ?? ""
    let studentPassword = request.parameters["password"] ?? ""
    
    for i in user {
        if studentName == i["name"] {
            if studentPassword == i["password"] {
                 try response.send(["status":"ok"]).end()
            } else {
                 try response.send(["status":"errorPassword"]).end()
            }
        } else {
            try response.send(["status":"noUserFound"]).end()
        }
    }
    
}

router.get("/courses/:student") { request, response, next in
    
    let studentName = request.parameters["student"] ?? ""
    
    if(studentName == "student1"){
        try response.send(["cours":[["course": "ruby",
                           "start": "9.00am",
                           "end": "11.00am",
                           "place": "Palais Brogniart",
                           "latitude": "48.8688356",
                           "longitude": "2.3414426",
                           "range": "100"],
                           ["course": "ruby",
                            "start": "11.15am",
                            "end": "1.15pm",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"],
                           ["course": "dev mobile",
                            "start": "2.15pm",
                            "end": "4.15pm",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"],
                           ["course": "dev mobile",
                            "start": "4.30pm",
                            "end": "6.30pm",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"]]
            ]).end()
    }
    else if(studentName == "student2"){
        try response.send(["cours":[["course": "market",
                           "start": "9.00am",
                           "end": "11.00am",
                           "place": "Palais Brogniart",
                           "latitude": "48.8688356",
                           "longitude": "2.3414426",
                           "range": "100"],
                           ["course": "market",
                            "start": "9.00am",
                            "end": "11.00am",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"],
                           ["course": "market",
                            "start": "9.00am",
                            "end": "11.00am",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"],
                           ["course": "market",
                            "start": "9.00am",
                            "end": "11.00am",
                            "place": "Palais Brogniart",
                            "latitude": "48.8688356",
                            "longitude": "2.3414426",
                            "range": "100"]]
            ]).end()
    } else {
        try response.redirect("/error").end()
    }
    
}

let port = 8080

Kitura.addHTTPServer(onPort: port, with: router)
Kitura.run()
