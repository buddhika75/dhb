package jsf;

import entity.Institution;
import jsf.util.JsfUtil;
import jsf.util.JsfUtil.PersistAction;
import bean.InstitutionFacade;
import entity.Category;
import entity.Participant;

import java.io.Serializable;
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
import org.primefaces.event.CaptureEvent;

@Named("institutionController")
@SessionScoped
public class InstitutionController implements Serializable {

    @EJB
    private bean.InstitutionFacade ejbFacade;
    private List<Institution> items = null;
    private List<Institution> categoryItems = null;
    private Institution selected;
    private Category category;
    private Participant selectedParcipant;

    public String toParticipant() {
        if (selected == null) {
            return "";
        }
        if (selected.getParticipants().size() > 1) {
            selectedParcipant = null;
            return "participants";
        } else {

            selectedParcipant = selected.getParticipants().get(0);
            return "participant";
        }
    }

    public InstitutionController() {
    }

    public Institution getSelected() {
        return selected;
    }

    public void setSelected(Institution selected) {
        this.selected = selected;
    }

    public Institution getInstitution(String name, Category cat, boolean createNew) {
        if (name.trim().equals("")) {
            return null;
        }
        String j;
        Map m = new HashMap();
        j = "select a "
                + " from Institution a "
                + " where (upper(a.name) =:n) and a.category=:c ";
        m.put("n", name.toUpperCase());
        m.put("c", cat);
        j += " order by a.name";
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        Institution ta = getFacade().findFirstBySQL(j, m);
        if (ta == null && createNew) {
            ta = new Institution();
            ta.setName(name);
            ta.setCategory(cat);
            getFacade().create(ta);
        }
        return ta;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private InstitutionFacade getFacade() {
        return ejbFacade;
    }

    public Institution prepareCreate() {
        selected = new Institution();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("InstitutionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void capturePhoto(CaptureEvent captureEvent) {
        if (selectedParcipant == null) {
            return;
        }
        try {
            getSelectedParcipant().setFileName("tem.jpeg");
            getSelectedParcipant().setFileType("image/jpeg");
            getSelectedParcipant().setBaImage(captureEvent.getData());
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Photo Captured. Click Next");
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

   

    public void clearPhoto() {
        if (selectedParcipant == null) {
            return;
        }

        selectedParcipant.setBaImage(null);
        update();
    }

    public void clearSignature() {
        if (selectedParcipant == null) {
            return;
        }
        selectedParcipant.setSignature(null);
        update();
    }
    
     public void registerForDayOne() {
        if (selectedParcipant == null) {
            return;
        }
        selectedParcipant.setFirstDay(true);
        update();
    }
     
     public void registerForDayTwo() {
        if (selectedParcipant == null) {
            return;
        }
        selectedParcipant.setSecondDay(true);
        update();
    }

    public String updateAndToPhoto() {
        getFacade().edit(selected);
        JsfUtil.addSuccessMessage("Details Updated. Take a Photo Now. Let few seconds to transfer the photo. Then Click Next.");
        return "participant_photo";
    }

    public String updateAndToSignature() {
        update();
        return "participant_signature";
    }

    public String updateAndToRegister() {
        update();
        return "participant_register";
    }

    public String saveOrUpdate() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Error");
            return "";
        }
        if (selected.getId() == null) {
            getFacade().create(selected);
            JsfUtil.addSuccessMessage("Saved");
        } else {
            getFacade().edit(selected);
            JsfUtil.addSuccessMessage("Saved");
        }
        return "";
    }

    public String saveOrUpdate(Institution ins) {
        if (ins == null) {
            return "";
        }
        if (ins.getId() == null) {
            getFacade().create(ins);
        } else {
            getFacade().edit(ins);
        }
        return "";
    }

    
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("InstitutionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("InstitutionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Institution> getItems() {
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

    public Institution getInstitution(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Institution> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Institution> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Institution> getCategoryItems() {
        String j = "select i from Institution i where i.category=:c order by i.name";
        Map m = new HashMap();
        m.put("c", category);
        categoryItems = getFacade().findBySQL(j, m);
        return categoryItems;
    }

    public void setCategoryItems(List<Institution> categoryItems) {
        this.categoryItems = categoryItems;
    }

    public void addNewParticipant() {
        getSelectedParcipant();
    }

    public Participant getSelectedParcipant() {
        return selectedParcipant;
    }

    public void setSelectedParcipant(Participant selectedParcipant) {
        this.selectedParcipant = selectedParcipant;
    }

    @FacesConverter(forClass = Institution.class)
    public static class InstitutionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstitutionController controller = (InstitutionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "institutionController");
            return controller.getInstitution(getKey(value));
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
            if (object instanceof Institution) {
                Institution o = (Institution) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Institution.class.getName()});
                return null;
            }
        }

    }

}
