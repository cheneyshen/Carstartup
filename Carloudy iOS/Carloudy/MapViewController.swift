//
//  MapViewController.swift
//  Carloudy
//
//  Created by Fei Shen on 2017/3/10.
//  Copyright © 2017年 Fei Shen. All rights reserved.
//

import Foundation
import UIKit
import Mapbox
import MapboxDirections
import MapboxNavigation
import MapboxNavigationUI
import MapboxGeocoder
import CoreLocation
import AVFoundation

class MapViewController: UIViewController, ParamsProtocol {

    
    // mapbox
    @IBOutlet var mapView: MGLMapView!
    let geocoder = Geocoder.shared
    let directions = Directions.shared
    
    let locationManager = CLLocationManager()
    var locValue: CLLocationCoordinate2D!
    var destination: CLLocationCoordinate2D?
    var stopValue: CLLocationCoordinate2D?
    var menuView: UIView?
    var isMenuOpen: Bool = false
    let menuSize: CGFloat = 0.8
    var topBuffer: CGFloat?
    var searchDelegate = SearchViewController()
    let appDelegate = UIApplication.shared.delegate as! AppDelegate
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationController?.setNavigationBarHidden(false, animated: true)
        
        topBuffer = (self.navigationController?.navigationBar.frame.height)! + UIApplication.shared.statusBarFrame.size.height

        // Ask for Authorisation from the User.
        self.locationManager.requestAlwaysAuthorization()
        
        // For use in foreground
        self.locationManager.requestWhenInUseAuthorization()
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
            locationManager.startUpdatingLocation()
        }
        
        mapView.delegate = self
        mapView.userTrackingMode = .follow
        //mapboxRoute()
        
        //设置代理
        searchDelegate.paramsProtocolDelegate = self
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getMessage(array: NSArray!) {
        
        let latitude=(array[0] as! NSString).doubleValue
        let longitude=(array[1] as! NSString).doubleValue
        if(array.count == 0)
        {
            destination = nil
        }
        destination = CLLocationCoordinate2D(latitude: latitude as! CLLocationDegrees, longitude: longitude as! CLLocationDegrees)
        
        print(destination)
        
    }
}

extension MapViewController: MGLMapViewDelegate {
    
    // get a route object and also draw the route on the map
    func mapboxRoute() {
        let waypoints = [
            Waypoint(
                coordinate: CLLocationCoordinate2D(latitude: 38.9131752, longitude: -77.0324047),
                name: "Mapbox"
            ),
            Waypoint(
                coordinate: CLLocationCoordinate2D(latitude: 38.8977, longitude: -77.0365),
                name: "White House"
            ),
            ]
        let options = RouteOptions(waypoints: waypoints, profileIdentifier: MBDirectionsProfileIdentifier.automobile)
        options.includesSteps = true
        
        _ = directions.calculate(options) { (waypoints, routes, error) in
            guard error == nil else {
                print("Error calculating directions: \(error!)")
                return
            }
            
            if let route = routes?.first, let leg = route.legs.first {
                print("Route via \(leg):")
                
                let distanceFormatter = LengthFormatter()
                let formattedDistance = distanceFormatter.string(fromMeters: route.distance)
                
                let travelTimeFormatter = DateComponentsFormatter()
                travelTimeFormatter.unitsStyle = .short
                let formattedTravelTime = travelTimeFormatter.string(from: route.expectedTravelTime)
                
                print("Distance: \(formattedDistance); ETA: \(formattedTravelTime!)")
                
                for step in leg.steps {
                    print("\(step.instructions)")
                    let formattedDistance = distanceFormatter.string(fromMeters: step.distance)
                    print("— \(formattedDistance) —")
                }
                
                let viewController = NavigationUI.routeViewController(for: route, directions: self.directions)
                self.present(viewController, animated: true, completion: nil)
                
                /*
                 if route.coordinateCount > 0 {
                 // Convert the route’s coordinates into a polyline.
                 var routeCoordinates = route.coordinates!
                 let routeLine = MGLPolyline(coordinates: &routeCoordinates, count: route.coordinateCount)
                 
                 // Add the polyline to the map and fit the viewport to the polyline.
                 self.mapView.addAnnotation(routeLine)
                 self.mapView.setVisibleCoordinates(&routeCoordinates, count: route.coordinateCount, edgePadding: .zero, animated: true)
                 }*/
                
                
            }
        }
    }
    
    func annotation() {
        let point = MGLPointAnnotation()
        point.coordinate = CLLocationCoordinate2D(latitude: 41.8315897, longitude: -87.6437102)
        point.title = "Home"
        point.subtitle = "3434 S Wallace St"
        mapView.addAnnotation(point)
    }
    
    func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
        // Always try to show a callout when an annotation is tapped.
        return true
    }
    
    @IBAction func didLongPress(_ sender: UILongPressGestureRecognizer) {
        guard sender.state == .began else {
            return
        }
        
        stopValue = mapView.convert(sender.location(in: mapView), toCoordinateFrom: mapView)
        print(stopValue?.latitude.description, stopValue?.longitude.description, separator:" ", terminator:".")
        
        // have to configure options and get the directions
        let waypoints = [
            Waypoint(
                coordinate: locValue,
                name: "Current Location"
            ),
            Waypoint(
                coordinate: CLLocationCoordinate2D(latitude: (stopValue?.latitude)!, longitude: (stopValue?.longitude)!),
                name: "New Stop"
            ),
            Waypoint(
                coordinate: CLLocationCoordinate2D(latitude:41.8315897, longitude: -87.6437102),
                name: "Mapbox"
            ),
            ]
        
        let options = RouteOptions(waypoints: waypoints, profileIdentifier: MBDirectionsProfileIdentifier.automobile)
        options.includesSteps = true
        
        _ = directions.calculate(options) { (waypoints, routes, error) in
            guard error == nil else {
                print("Error calculating directions: \(error!)")
                return
            }
            let viewController = NavigationUI.routeViewController(for: (routes?[0])!, directions: self.directions)
            self.present(viewController, animated: true, completion: nil)
            
        }

    }
    
    @IBAction func didToggleNavigation(_ sender: Any) {
//        if isInNavigationMode {
//            endNavigation()
//        } else {
//            startNavigation(userRoute!)
//        }
//        isInNavigationMode = !isInNavigationMode
    }
}

extension MapViewController: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        locValue = (manager.location?.coordinate)!
    }
    
}
