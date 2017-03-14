//
//  SearchViewController.swift
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

//protocol GetMessageDelegate:NSObjectProtocol
//{
//    //回调方法 传一个NSArray类型的值
//    func getMessage(controller:SearchViewController,array:NSArray!)
//
//}
class SearchViewController: UIViewController {
    
    let geocoder = Geocoder.shared
    let directions = Directions.shared
    var searchResults: [GeocodedPlacemark] = []
    
    let locationManager = CLLocationManager()
    var locValue: CLLocationCoordinate2D!
    var finalValue: Waypoint!
    var paramsProtocolDelegate : ParamsProtocol!
    
    @IBOutlet weak var searchText: UITextField!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        searchText.addTarget(self, action: #selector(searchTextChanged(_:)), for: UIControlEvents.editingChanged)
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
            locationManager.startUpdatingLocation()
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func mapboxGeocoder(queryText: String) {
        let options = ForwardGeocodeOptions(query: queryText)
        options.allowedISOCountryCodes = ["US"]
        //options.focalLocation = locationManager.location
        options.allowedScopes = [.address, .pointOfInterest]
        
        let _ = geocoder.geocode(options,
                                 completionHandler: { placemarks, attribution, error in
                                    if let unwrapped = placemarks {
                                        self.searchResults = unwrapped
                                    } else {
                                        self.searchResults = []
                                    }
                                    self.tableView.reloadData()
        })
    }
    
}

extension SearchViewController: UITextFieldDelegate {
    func searchTextChanged(_ textField: UITextField) {
        mapboxGeocoder(queryText: (textField.text ?? ""))
    }
}

extension SearchViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        locValue = (manager.location?.coordinate)!
    }
}

extension SearchViewController: UITableViewDataSource, UITableViewDelegate {
    // MARK: - Table view data source
    
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 5
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        // Configure the cell...
        // must + 1, fixed a bug
        if (indexPath.row + 1 < searchResults.capacity) {
            cell.textLabel?.text = searchResults[indexPath.row].qualifiedName
        } else {
            cell.textLabel?.text = ""
        }
        return cell
    }
    
//    func delegateStart()
//    {
//        let places:NSArray!
//        if finalValue == nil {
//            places = [
//                "", //hard code
//                ""
//            ]
//        }
//        else {
//            places = [
//                self.finalValue.coordinate.latitude.description,
//                self.finalValue.coordinate.longitude.description
//            ]
//        }
//        self.paramsProtocolDelegate?.returmParameArray(array:places)
//        
//    }
    
    func delegateStart() {
        let places:NSArray!
        places = ["\(self.finalValue.coordinate.latitude)",
            "\(self.finalValue.coordinate.longitude)"]
        self.paramsProtocolDelegate?.getMessage(array: places)
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
//        
//                self.finalValue = Waypoint(
//                    coordinate: searchResults[indexPath.row].location.coordinate,
//                    name: searchResults[indexPath.row].qualifiedName
//                )
//        
//        self.navigationController!.popViewController(animated: true)
        let cell = tableView.cellForRow(at: indexPath)
        // have to configure options and get the directions
        let waypoints = [
            Waypoint(
                coordinate: locValue,
                name: "Current Location"
            ),
            Waypoint(
                coordinate: searchResults[indexPath.row].location.coordinate,
                name: searchResults[indexPath.row].qualifiedName
            ),
            ]
        
        self.finalValue = Waypoint(
            coordinate: searchResults[indexPath.row].location.coordinate,
            name: searchResults[indexPath.row].qualifiedName
        )
        print("FinalValue 的值为 \(finalValue)")
        
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
        
        //print(cell?.textLabel?.text)
        //print("search results: " + searchResults[indexPath.row].qualifiedName)
        //print(searchResults[indexPath.row].location.coordinate)
        
    }
}
