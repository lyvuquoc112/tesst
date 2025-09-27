# Smart Clinic Management System — Database Schema (MySQL)

This document describes the relational schema used by the Smart Clinic Management System.  
It supports core flows for **Doctor**, **Patient**, **Appointment**, **Prescription**, and **Admin**, plus **Doctor Time Slots** for booking.

---

## Tables Overview

### 1) `doctor`
| Field         | Type           | Constraints                                      |
|---------------|----------------|--------------------------------------------------|
| `doctor_id`   | INT            | **PK**, AUTO_INCREMENT                           |
| `full_name`   | VARCHAR(100)   | NOT NULL                                         |
| `specialty`   | VARCHAR(50)    | NOT NULL, indexed                                |
| `email`       | VARCHAR(100)   | **UNIQUE**, NOT NULL                             |
| `phone`       | VARCHAR(15)    | UNIQUE NULL                                      |
| `password_hash` | VARCHAR(255) | NOT NULL                                         |
| `created_at`  | DATETIME       | DEFAULT CURRENT_TIMESTAMP                        |

**Notes:** `email` must be unique; `specialty` is searched frequently → index.

---

### 2) `patient`
| Field         | Type           | Constraints                                      |
|---------------|----------------|--------------------------------------------------|
| `patient_id`  | INT            | **PK**, AUTO_INCREMENT                           |
| `full_name`   | VARCHAR(100)   | NOT NULL                                         |
| `dob`         | DATE           | NOT NULL                                         |
| `phone`       | VARCHAR(15)    | UNIQUE NULL                                      |
| `email`       | VARCHAR(100)   | UNIQUE NULL                                      |
| `password_hash` | VARCHAR(255) | NOT NULL                                         |
| `created_at`  | DATETIME       | DEFAULT CURRENT_TIMESTAMP                        |

---

### 3) `appointment`
| Field              | Type         | Constraints                                                                 |
|--------------------|--------------|-----------------------------------------------------------------------------|
| `appointment_id`   | INT          | **PK**, AUTO_INCREMENT                                                      |
| `doctor_id`        | INT          | **FK** → `doctor(doctor_id)` ON DELETE CASCADE                              |
| `patient_id`       | INT          | **FK** → `patient(patient_id)` ON DELETE CASCADE                            |
| `appointment_time` | DATETIME     | NOT NULL, indexed                                                           |
| `status`           | ENUM('PENDING','CONFIRMED','CANCELLED','DONE') | DEFAULT 'PENDING'                     |
| `notes`            | TEXT         | NULL                                                                        |
| `created_at`       | DATETIME     | DEFAULT CURRENT_TIMESTAMP                                                   |

**Notes:** 1 doctor : N appointments; 1 patient : N appointments.  
We keep a status lifecycle to support admin/doctor actions.

---

### 4) `prescription`
| Field             | Type         | Constraints                                                                 |
|-------------------|--------------|-----------------------------------------------------------------------------|
| `prescription_id` | INT          | **PK**, AUTO_INCREMENT                                                      |
| `appointment_id`  | INT          | **FK** → `appointment(appointment_id)` ON DELETE CASCADE                    |
| `description`     | TEXT         | NOT NULL                                                                    |
| `created_at`      | DATETIME     | DEFAULT CURRENT_TIMESTAMP                                                   |

**Notes:** One appointment may generate multiple prescriptions.

---

### 5) `admin`
| Field           | Type          | Constraints                                    |
|-----------------|---------------|------------------------------------------------|
| `admin_id`      | INT           | **PK**, AUTO_INCREMENT                         |
| `full_name`     | VARCHAR(100)  | NOT NULL                                       |
| `email`         | VARCHAR(100)  | **UNIQUE**, NOT NULL                           |
| `password_hash` | VARCHAR(255)  | NOT NULL                                       |
| `created_at`    | DATETIME      | DEFAULT CURRENT_TIMESTAMP                      |

---

### 6) `doctor_time_slot`
| Field          | Type      | Constraints                                                            |
|----------------|-----------|------------------------------------------------------------------------|
| `slot_id`      | INT       | **PK**, AUTO_INCREMENT                                                 |
| `doctor_id`    | INT       | **FK** → `doctor(doctor_id)` ON DELETE CASCADE                         |
| `start_time`   | DATETIME  | NOT NULL                                                               |
| `end_time`     | DATETIME  | NOT NULL                                                               |
| `is_available` | TINYINT(1)| DEFAULT 1 (true)                                                       |

**Notes:** Represents doctor `availableTimes`. The application ensures `start_time < end_time` and no overlaps per doctor.

---

## Relationships

- **Doctor 1—N Appointment** (via `doctor_id`)  
- **Patient 1—N Appointment** (via `patient_id`)  
- **Appointment 1—N Prescription** (via `appointment_id`)  
- **Doctor 1—N TimeSlot** (via `doctor_id`)  

---

## ERD (Mermaid)

```mermaid
erDiagram
    DOCTOR ||--o{ APPOINTMENT : has
    PATIENT ||--o{ APPOINTMENT : books
    APPOINTMENT ||--o{ PRESCRIPTION : generates
    DOCTOR ||--o{ DOCTOR_TIME_SLOT : offers

    DOCTOR {
      int doctor_id PK
      varchar full_name
      varchar specialty
      varchar email
      varchar phone
      varchar password_hash
      datetime created_at
    }
    PATIENT {
      int patient_id PK
      varchar full_name
      date dob
      varchar phone
      varchar email
      varchar password_hash
      datetime created_at
    }
    APPOINTMENT {
      int appointment_id PK
      int doctor_id FK
      int patient_id FK
      datetime appointment_time
      enum status
      text notes
      datetime created_at
    }
    PRESCRIPTION {
      int prescription_id PK
      int appointment_id FK
      text description
      datetime created_at
    }
    DOCTOR_TIME_SLOT {
      int slot_id PK
      int doctor_id FK
      datetime start_time
      datetime end_time
      tinyint is_available
    }
