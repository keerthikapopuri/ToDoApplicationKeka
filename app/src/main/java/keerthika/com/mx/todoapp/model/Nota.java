package keerthika.com.mx.todoapp.model;

public class Nota {



    private String id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String date;




    public Nota(String id, String name, String description, String status,String priority,String category,String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority=priority;
        this.category=category;
        this.date=date;


    }
    public Nota(){
        this.id = "";
        this.name = "";
        this.description = "";
        this.status = "";
        this.priority= "";
        this.category= "";
        this.date= "";

    }
    public Nota(String id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
