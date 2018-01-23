//
//  ViewController.swift
//  GeoLoc
//
//  Created by eemi on 13/12/2017.
//  Copyright © 2017 eemi. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit

class SecondViewController: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate {
    
    
    
    @IBOutlet weak var mapView: MKMapView!
    
    var locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view, typically from a nib.
        
        locationManager.delegate = self
        locationManager.requestAlwaysAuthorization()
        mapView.delegate = self
        mapView.showsUserLocation = true
        mapView.userTrackingMode = .follow
        
        let brognart = CLCircularRegion(center: CLLocationCoordinate2D(latitude: 48.86915, longitude: 2.341407), radius: 25, identifier: "school")
        let circle = MKCircle(center: brognart.center, radius: brognart.radius)
        mapView.add(circle)
        
        brognart.notifyOnExit = true
        brognart.notifyOnEntry = true
        
        locationManager.startMonitoring(for: brognart)
        locationManager.startUpdatingLocation()
        // locationManager.startMonitoringSignificantLocationChanges()
        
        //        let region3 = CLBeaconRegion(proximityUUID: UUID(uuidString:"f2a74fc4-7625-44db-9b08-cb7e130b2029")!, identifier: "uBeacon3")
        //        locationManager.startRangingBeacons(in: region3)
        
    }
    
    // Create
    override func viewDidAppear(_ animated: Bool) {
        mapView.addAnnotation(PersoAnnotation(coordinate: CLLocationCoordinate2D(latitude: 48.86915, longitude: 2.341407), title: "Palais"))
        mapView.addAnnotation(PersoAnnotation(coordinate: CLLocationCoordinate2D(latitude: 48.87146019999999, longitude: 2.3417991999999686), title: "Pano"))
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        return nil
    }
    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        let circleRenderer = MKCircleRenderer(overlay: overlay)
        circleRenderer.fillColor = UIColor.blue.withAlphaComponent(0.2)
        circleRenderer.strokeColor = UIColor.blue
        circleRenderer.lineWidth = 1
        return circleRenderer
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        // print("***location:*** \(locations)")
        for r in locationManager.monitoredRegions {
            print("state : \(locationManager.requestState(for: r))")
            print("\(locations) is in \(r)? ")
        }
    }
    
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        //print("***error*** \(error)")
    }
    
    public func locationManager(_ manager: CLLocationManager, didEnterRegion region: CLRegion) {
        
        if region.identifier == "school" {
            // print("t'es dedans")
            let alertController = UIAlertController(title: "Beacon", message: "Tu rentres", preferredStyle: .alert)
            let OKAction = UIAlertAction(title: "OK", style: .default) { (action:UIAlertAction!) in}
            alertController.addAction(OKAction)
            self.present(alertController, animated: true, completion:nil)
            
            let region3 = CLBeaconRegion(proximityUUID: UUID(uuidString:"f2a74fc4-7625-44db-9b08-cb7e130b2029")!, identifier: "uBeacon3")
            
            locationManager.startRangingBeacons(in: region3)
            // locationManager.startMonitoring(for: region3)
            
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didExitRegion region: CLRegion) {
        if region.identifier == "school" {
            // print("tu sors")
            let alertController = UIAlertController(title: "Beacon", message: "Tu sors", preferredStyle: .alert)
            let OKAction = UIAlertAction(title: "OK", style: .default) { (action:UIAlertAction!) in}
            alertController.addAction(OKAction)
            self.present(alertController, animated: true, completion:nil)
            for ranging in locationManager.rangedRegions {
                if let beacon = ranging as? CLBeaconRegion {
                    locationManager.stopRangingBeacons(in: beacon)
                }
            }
        }
    }
    
    public func locationManager(_ manager: CLLocationManager, didRangeBeacons beacons: [CLBeacon], in region: CLBeaconRegion) {
        for b in beacons {
            var prox = "Unknown"
            switch b.proximity {
            case .near:
                prox = "We are close by"
            case .immediate:
                prox = "We are close by"
            default:
                prox = "We are not close"
            }
            let alertController = UIAlertController(title: "Beacon is detected", message: prox, preferredStyle: .alert)
            let OKAction = UIAlertAction(title: "OK", style: .default) { (action:UIAlertAction!) in}
            alertController.addAction(OKAction)
            self.present(alertController, animated: true, completion:nil)
            //print("Beacon \(b.minor) : \(prox)")
        }
    }
    
}

// Create abstract class MKAnnotation with coordinate and title in parameters
class PersoAnnotation : NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    var title: String?
    init(coordinate: CLLocationCoordinate2D, title: String) {
        self.coordinate = coordinate
        self.title = title
    }
}




