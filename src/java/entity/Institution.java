/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author divudi_lk
 */
@Entity
public class Institution implements Serializable {

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Participant> participants;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToOne
    private Category category;
    private String backgoundColour;
    private String foregroundColour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Institution)) {
            return false;
        }
        Institution other = (Institution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Institution[ id=" + id + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBackgoundColour() {
        return backgoundColour;
    }

    public void setBackgoundColour(String backgoundColour) {
        this.backgoundColour = backgoundColour;
    }

    public String getForegroundColour() {
        return foregroundColour;
    }

    public void setForegroundColour(String foregroundColour) {
        this.foregroundColour = foregroundColour;
    }

    public List<Participant> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<>();
        }
        if (participants.isEmpty()) {
            Participant p = new Participant();
            p.setName("");
            p.setInstitution(this);
            participants.add(p);
        }
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

}
