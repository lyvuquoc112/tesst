# Smart Clinic Management System - Database Schema (MySQL)

This schema is designed to support the core functionalities of the Smart Clinic Management System, including Doctor, Patient, Appointment, and Prescription management.

---

## Tables Overview

### 1. Doctor
| Field           | Type         | Constraints                       |
|-----------------|--------------|-----------------------------------|
| doctor_id       | INT          | PRIMARY KEY, AUTO_INCREMENT       |
| full_name       | VARCHAR(100) | NOT NULL                          |
| specialty       | VARCHAR(50)  | NOT NULL                          |
| email           | VARCHAR(100) | UNIQUE, NOT NULL                  |
| password_hash   | VARCHAR(255) | NOT NULL                          |

---

### 2. Patient
| Field           | Type         | Constraints                       |
|-----------------|--------------|-----------------------------------|
| patient_id      | INT          | PRIMARY KEY, AUTO_INCREMENT       |
| full_name       | VARCHAR(100) | NOT NULL                          |
| dob             | DATE         | NOT NULL                          |
| phone           | VARCHAR(15)  | UNIQUE                            |
| email           | VARCHAR(100) | UNIQUE                            |
| password_hash   | VARCHAR(255) | NOT NULL                          |

---

### 3. Appointment
| Field            | Type         | Constraints                                   |
|------------------|--------------|-----------------------------------------------|
| appointment_id   | INT          | PRIMARY KEY, AUTO_INCREMENT                   |
| doctor_id        | INT          | FOREIGN KEY REFERENCES Doctor(doctor_id)      |
| patient_id       | INT          | FOREIGN KEY REFERENCES Patient(patient_id)    |
| appointment_time | DATETIME     | NOT NULL                                      |
| status           | VARCHAR(20)  | DEFAULT 'PENDING'                             |

---

### 4. Prescription
| Field           | Type         | Constraints                                      |
|-----------------|--------------|--------------------------------------------------|
| prescription_id | INT          | PRIMARY KEY, AUTO_INCREMENT                      |
| appointment_id  | INT          | FOREIGN KEY REFERENCES Appointment(appointment_id) |
| description     | TEXT         | NOT NULL                                         |
| created_at      | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP                        |

---

### 5. Admin (Optional)
| Field        | Type         | Constraints                       |
|--------------|--------------|-----------------------------------|
| admin_id     | INT          | PRIMARY KEY, AUTO_INCREMENT       |
| full_name    | VARCHAR(100) | NOT NULL                          |
| email        | VARCHAR(100) | UNIQUE, NOT NULL                  |
| password_hash| VARCHAR(255) | NOT NULL                          |

---

## Relationships
- **Doctor → Appointment**: One doctor can have many appointments.  
- **Patient → Appointment**: One patient can have many appointments.  
- **Appointment → Prescription**: One appointment can have multiple prescriptions.  
- **Admin** manages Doctors and Patients (system-level).  

---

## ERD (Entity Relationship Diagram)

```mermaid
erDiagram
    DOCTOR ||--o{ APPOINTMENT : has
    PATIENT ||--o{ APPOINTMENT : books
    APPOINTMENT ||--o{ PRESCRIPTION : generates
