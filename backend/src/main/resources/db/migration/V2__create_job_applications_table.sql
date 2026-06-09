CREATE TABLE job_applications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    job_title VARCHAR(150) NOT NULL,
    location VARCHAR(150),
    contract_type VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'SAVED',
    source VARCHAR(100),
    application_url TEXT,
    notes TEXT,
    applied_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_job_applications_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_job_applications_user_id
ON job_applications(user_id);

CREATE INDEX idx_job_applications_status
ON job_applications(status);