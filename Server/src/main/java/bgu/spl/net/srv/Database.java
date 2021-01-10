package bgu.spl.net.srv;


import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database<T> {
	private static Database instance;
	private ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> clientsInfo; //The main dataBase of Clients by name to there details .
	private HashMap<Integer, Course> coursesInfo;// Getting num of course and giving the clients that are already registered to it .
	private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionIdMap;
	private HashMap<Integer, String> connectionsToNames;
	private HashMap<String, Integer> NamesToConnections;
	private int ID;

	//to prevent user from creating new Database
	public Database() throws IOException {
		clientsInfo =new ConcurrentHashMap<>();
		coursesInfo =new HashMap<>();
		connectionIdMap= new ConcurrentHashMap<>();
		connectionsToNames= new HashMap<>();
		NamesToConnections= new HashMap<>();
		ID=1;
		initialize("./Courses.txt");
	}
	private static class DatabaseHolder { // Singleton
		private static Database instance;

		static {
			try {
				instance = new Database();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Database getInstance() { // Singleton
		if (instance == null) {
			instance = DatabaseHolder.instance;
		}
		return instance;
	}


	/**
	 * loades the courses from the file path specified 
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) throws IOException {
		coursesInfo=initialize(coursesFilePath,0);
		if (coursesInfo!=null)
			return true;
		return false;
	}

		HashMap<Integer,Course> initialize(String coursesFilePath, int x) throws IOException {
		HashMap<Integer, Course> copyCoursesInfo = new HashMap<>();
		// Getting num of course and giving the clients that are already registered to it .
		Integer courseNum;
		String courseName;
		List<Integer> kdamCoursesList;
		int numOfMaxStudents;
		Course courseToImport;
		try {
			File file = new File(coursesFilePath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			while ((st = br.readLine()) != null){
				kdamCoursesList = new LinkedList<>();
				String[] dataByCategory = st.split("\\|");
				courseNum = Integer.parseInt(dataByCategory[0]);
				courseName = dataByCategory[1];
				String kdamCoursesListFirst = dataByCategory[2];
				if (!kdamCoursesListFirst.equals("[]")) {
					kdamCoursesListFirst = kdamCoursesListFirst.substring(1, kdamCoursesListFirst.length() - 1);
					String[] kdamCoursesListBefore = kdamCoursesListFirst.split(","); // [122] [134]
					for (int i = 0; i < kdamCoursesListBefore.length; i++) {
						String courseString = kdamCoursesListBefore[i];
						if (courseString.length() != 0) {
							Integer courseToAdd = Integer.valueOf(courseString);
							kdamCoursesList.add(courseToAdd);
						}
					}
				}
				numOfMaxStudents = Integer.parseInt(dataByCategory[3]);
				courseToImport = new Course(courseNum,courseName,numOfMaxStudents,kdamCoursesList);
				copyCoursesInfo.put(courseNum,courseToImport);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred. FileNotFoundException");
			e.printStackTrace();
		}

		return copyCoursesInfo;
	}

	public ConcurrentHashMap<String, ConcurrentLinkedQueue<User>> getClientsInfo() {
		return clientsInfo;
	}
	public void setClientsInfo(String userName, User newUser) {
		ConcurrentLinkedQueue<User> NEWuser=new ConcurrentLinkedQueue<>();
		NEWuser.add(newUser);
		clientsInfo.put(userName,NEWuser);
	}

	public User getUser(String userName){
		if (userName==null)
			return null;
		if (clientsInfo.size()==0)
			return null;

		if (clientsInfo.get(userName)!=null)
			return clientsInfo.get(userName).element();
		return null;
	}

	public Course getCourse(Integer courseNum) {
		if (coursesInfo.size()==0)
			return null;
		if (coursesInfo.get(courseNum)!=null)
			return coursesInfo.get(courseNum);
		return null;
	}

	public ConcurrentHashMap<Integer, ConnectionHandler<T>> getConnectionIdMap() {
		return connectionIdMap;
	}

	public HashMap<Integer,Course> getCoursesInfo() {
		return coursesInfo;
	}

	public List<Integer> getKdamCourses(int courseNum) {
		if (coursesInfo.size()==0)
			return null;
		if (coursesInfo.get(courseNum)!=null) {
			Course course=coursesInfo.get(courseNum);
			return course.getKdamCourses();
		}
		return null;
	}

	public void addConnection(int connectionId,ConnectionHandler<T> connectionHandler){
		this.connectionIdMap.put(connectionId,connectionHandler);
	}
	public void setConnectionstoNames(int connectionId,String name){
		connectionsToNames.put(connectionId,name);
	}
	public String getConnectionstoNames(int connectionId){
		if (connectionsToNames.size()==0)
			return null;
		if (connectionsToNames.get(connectionId)!=null)
			return connectionsToNames.get(connectionId);
		return null;
	}
	public void setNamesToConnections(String name,int connectionId){
		NamesToConnections.put(name,connectionId);
	}
	public Integer getNamesToConnections(String name){
		if (NamesToConnections.size()==0)
			return null;
		if (NamesToConnections.get(name)!=null)
			return NamesToConnections.get(name);
		return null;
	}
	public void setId()
	{
		ID++;
	}
	public int getId()
	{
		return ID;
	}

}

