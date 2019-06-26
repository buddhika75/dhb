package jsf;

import entity.Participant;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import bean.ParticipantFacade;
import entity.Category;
import entity.Gender;
import entity.Institution;
import entity.RoomType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.primefaces.model.UploadedFile;

@Named("participantController")
@SessionScoped
public class ParticipantController implements Serializable {

    @EJB
    private bean.ParticipantFacade ejbFacade;
    
    @Inject
    CategoryController categoryController;
    @Inject
    InstitutionController institutionController;
    
    private List<Participant> items = null;
    private Participant selected;
    
    private UploadedFile file;

    public ParticipantController() {
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public InstitutionController getInstitutionController() {
        return institutionController;
    }

    public UploadedFile getFile() {
        return file;
    }
    
    
    
    public String importProjectsFromExcel() {
        String strCat;
        String strIns;
        String strName;
        String strDesignation;
        String strPhone;
        String strEmail;
        String strGender;
        String strRoomType;
        String strStay;

       
        Institution ins;
        Category category;
        
        File inputWorkbook;
        Workbook w;
        Cell cell;
        InputStream in;

        int startRow = 1;

        JsfUtil.addSuccessMessage(file.getFileName());

        try {
            JsfUtil.addSuccessMessage(file.getFileName());
            in = file.getInputstream();
            File f;
            f = new File(Calendar.getInstance().getTimeInMillis() + file.getFileName());
            FileOutputStream out = new FileOutputStream(f);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            inputWorkbook = new File(f.getAbsolutePath());

            JsfUtil.addSuccessMessage("Excel File Opened");
            w = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = w.getSheet(0);

            for (int i = startRow; i < sheet.getRows(); i++) {

                

                Map m = new HashMap();

                //Category
                cell = sheet.getCell(0, i);
                strCat = cell.getContents();
                category = categoryController.getCategory(strCat, true);

                cell = sheet.getCell(1, i);
                strIns = cell.getContents();
                ins = institutionController.getInstitution(strIns, category, true);
                
                Participant np = ins.getParticipants().get(0);
                
                cell = sheet.getCell(2, i);
                strName = cell.getContents();
                np.setName(strName);

                cell = sheet.getCell(3, i);
                strDesignation = cell.getContents();
                np.setDesignation(strDesignation);

                cell = sheet.getCell(4, i);
                strPhone = cell.getContents();
                np.setPhone(strPhone);

                cell = sheet.getCell(5, i);
                strEmail = cell.getContents();
                np.setEmail(strEmail);

                cell = sheet.getCell(6, i);
                strGender = cell.getContents();
                if(strGender.equalsIgnoreCase("Female")){
                    np.setGender(Gender.Female);
                }else{
                    np.setGender(Gender.Male);
                }
                
                cell = sheet.getCell(7, i);
                strRoomType = cell.getContents();
                if(strRoomType.equalsIgnoreCase("Double")){
                    np.setRoomType(RoomType.Double);
                }else{
                    np.setRoomType(RoomType.Single);
                }
                
                cell = sheet.getCell(7, i);
                strStay = cell.getContents();
                if(strStay.equalsIgnoreCase("Yes")){
                    np.setOvernightStay(true);
                }else{
                    np.setOvernightStay(false);
                }
                
                institutionController.saveOrUpdate(ins);

            }

            JsfUtil.addSuccessMessage("Succesful. All the data in Excel File Impoted to the database");
            return "";
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex.getMessage());
            return "";
        } catch (BiffException e) {
            JsfUtil.addErrorMessage(e.getMessage());
            return "";
        }
    }

    public Participant getSelected() {
        return selected;
    }

    public void setSelected(Participant selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ParticipantFacade getFacade() {
        return ejbFacade;
    }

    public Participant prepareCreate() {
        selected = new Participant();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ParticipantCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ParticipantUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ParticipantDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Participant> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Participant getParticipant(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Participant> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Participant> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Participant.class)
    public static class ParticipantControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ParticipantController controller = (ParticipantController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "participantController");
            return controller.getParticipant(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Participant) {
                Participant o = (Participant) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Participant.class.getName()});
                return null;
            }
        }

    }

}
