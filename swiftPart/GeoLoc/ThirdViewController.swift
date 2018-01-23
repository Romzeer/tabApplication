//
//  ThirdViewController.swift
//  GeoLoc
//
//  Created by Laurent Maximin on 22/01/2018.
//  Copyright © 2018 eemi. All rights reserved.
//

import UIKit

class ThirdViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    
    var courses: [Course]? = []
    

    override func viewDidLoad() {
        super.viewDidLoad()
        fetchCourses()
        // Do any additional setup after loading the view.
    }
    
    func fetchCourses(){
        let urlRequest = URLRequest(url: URL(string: "http://127.0.0.1:8080/courses/student1")!)
        
        let task = URLSession.shared.dataTask(with: urlRequest) { (data,response,error) in
            
            if error != nil {
                print(error)
                return
            }
            
            self.courses = [Course]()

            do {
                let json = try JSONSerialization.jsonObject(with: data!, options: .mutableContainers) as! [String : AnyObject]
                if let coursesFromJson = json["cours"] as? [[String : AnyObject]] {
                    for courseFromJson in coursesFromJson {
                        let course = Course()

                        if let title = courseFromJson["course"] as? String, let place = courseFromJson["place"] as? String, let start = courseFromJson["start"] as? String, let end = courseFromJson["end"] as? String {

                            
                            course.course = title
                            course.place = place
                            course.start = start
                            course.end = end
                            course.presence = "NA"
                        }
                        self.courses?.append(course)
                    }
                }
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
                
            } catch let error {
                print(error)
            }
            
            
        }
        
        task.resume()
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CourseTableViewCell", for: indexPath) as! CourseTableViewCell
        
        cell.course.text = self.courses?[indexPath.item].course
        cell.place.text = self.courses?[indexPath.item].place
        cell.start.text = self.courses?[indexPath.item].start
        cell.end.text = self.courses?[indexPath.item].end
        cell.presence.text = self.courses?[indexPath.item].presence
        print(self.courses?[indexPath.item].presence)

        
        return cell
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.courses?.count ?? 0
    }

}
