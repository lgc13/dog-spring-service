# Checks if db is created, and if not, creates it
database="dogsdb"
psql -tc "SELECT 1 FROM pg_database WHERE datname = '$database'" | grep -q 1 || psql -tc "CREATE DATABASE $database"