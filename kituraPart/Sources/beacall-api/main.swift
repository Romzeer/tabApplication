import Kitura
import SwiftyJSON

let router = Router()

router.all("/", middleware: BodyParser(), StaticFileServer(path: "./Public"))

router.get("/") { request, response, next in
    
    try response.send(["9h-11h": "Panoramas"]).end()
}

let port = 8080

Kitura.addHTTPServer(onPort: port, with: router)
Kitura.run()
