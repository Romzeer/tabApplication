//
//  FirstViewController.swift
//  tabApp
//
//  Created by Quentin Faure on 10/01/2018.
//  Copyright © 2018 Quentin Faure. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
import Foundation

class FirstViewController: UIViewController, CLLocationManagerDelegate {
    
    var pins: [Pin] = []
    
    @IBOutlet weak var mapView: MKMapView!
    
    var locManager: CLLocationManager?
    
    func loadInitialData() {
        
        // 1
        guard let fileName = Bundle.main.path(forResource: "location", ofType: "json")
            else { return }
        let optionalData = try? Data(contentsOf: URL(fileURLWithPath: fileName))
        
        guard
            let data = optionalData,
            // 2
            let json = try? JSONSerialization.jsonObject(with: data),
            // 3
            let dictionary = json as? [String: Any],
            // 4
            let works = dictionary["data"] as? [[Any]]
            else { return }
        // 5
        let validWorks = works.flatMap { Pin(json: $0) }
        pins.append(contentsOf: validWorks)
        print(validWorks)
    }
    
    override func viewDidLoad() {
        //        var pins: [Pin] = []
        super.viewDidLoad()
        //geolocalisation
        locManager = CLLocationManager()
        locManager?.delegate = self
        locManager?.requestWhenInUseAuthorization()
        locManager?.startUpdatingLocation()
        mapView.showsUserLocation = true
        
        //        mapView.mapType = .hybrid
        mapView.showsCompass = true
        mapView.showsScale = true
        
//         show pin on map
                let eemi = Pin(title: "Ecole Européenne des Métiers de l'Internet",
                                      locationName: "EEMI",
                                      discipline: "School",
                                      coordinate: CLLocationCoordinate2D(latitude: 48.8688356, longitude: 2.3414426))
                mapView.addAnnotation(eemi)
        
        let panos = Pin(title: "Panoramas",
                        locationName: "Les Panoramas",
                        discipline: "Hub",
                        coordinate: CLLocationCoordinate2D(latitude: 48.870537, longitude: 2.342358))
        mapView.addAnnotation(panos)
        
        loadInitialData()
        mapView.addAnnotations(pins)
        print(pins)
    }
    
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        
        let location:CLLocation = locations[0]
        
        let latitudeUtilisateur = location.coordinate.latitude
        let longitudeUtilisateur = location.coordinate.longitude
        
        let latDelta:CLLocationDegrees = 0.01
        let lngDelta:CLLocationDegrees = 0.01
        
        let span:MKCoordinateSpan = MKCoordinateSpanMake(latDelta, lngDelta)
        
        let coordinate:CLLocationCoordinate2D = CLLocationCoordinate2DMake(latitudeUtilisateur, longitudeUtilisateur)
        
        
        let region:MKCoordinateRegion = MKCoordinateRegionMake(coordinate, span)
        
        
        mapView.setRegion(region, animated: true)
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

extension FirstViewController: MKMapViewDelegate {
    // 1
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        // 2
        guard let annotation = annotation as? Pin else { return nil }
        // 3
        let identifier = "marker"
        var view: MKMarkerAnnotationView
        // 4
        if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier)
            as? MKMarkerAnnotationView {
            dequeuedView.annotation = annotation
            view = dequeuedView
        } else {
            // 5
            view = MKMarkerAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            view.canShowCallout = true
            view.calloutOffset = CGPoint(x: -5, y: 5)
            view.rightCalloutAccessoryView = UIButton(type: .detailDisclosure)
        }
        return view
    }
}

