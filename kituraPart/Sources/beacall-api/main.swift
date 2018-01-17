import Kitura

let router = Router()

router.all("/", middleware: BodyParser(), StaticFileServer(path: "./Public"))

router.get("/:student") { request, response, next in
    
    let studentName = request.parameters["student"] ?? ""
    
    if(studentName == "student1"){
        try response.send(["course": "ruby",
                           "start": "9.00am",
                           "end": "11.00am",
                           "latitude": "48.8688356",
                           "longitude": "2.3414426",
                           "range": "500meters"]).end()
    }
    else if(studentName == "student2"){
        try response.send(["course": "market",
                           "start": "9.00am",
                           "end": "11.00am",
                           "latitude": "48.8688356",
                           "longitude": "2.3414426",
                           "range": "500meters"]).end()
    } else {
        try response.redirect("/error").end()
    }
    
}

let port = 8080

Kitura.addHTTPServer(onPort: port, with: router)
Kitura.run()
