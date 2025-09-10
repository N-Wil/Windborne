/* 
 * stoplight.c
 *
 * 31-1-2003 : GWA : Stub functions created for CS161 Asst1.
 *
 * NB: You can use any synchronization primitives available to solve
 * the stoplight problem in this file.
 */


/*
 * 
 * Includes
 *
 */

#include <types.h>
#include <lib.h>
#include <test.h>
#include <thread.h>
#include <synch.h>

//global variables

struct lock *lockAB;
struct lock *lockBC;
struct lock *lockCA;
struct lock *tempy;
//truck locks
struct lock *tlA;
struct lock *tlB;
struct lock *tlC;

//number of cars on a route
int carsA;
int carsB;
int carsC;

/*
 *
 * Constants
 *
 */

/*
 * Number of vehicles created.
 */

#define NVEHICLES 20


/*
 *
 * Function Definitions
 *
 */

//message fragments
const char* vehicles[] = {"Car", "Truck"};
const char* routes[] = {"A", "B", "C"};
const char* directions[] = {"left", "right"};
const char* intersection[] = {"AB", "BC", "CA"};


//functions for easier message printing
void message_approach(const char* vtype, int vnumber, const char* route, const char* turn_direction){
	kprintf("%s %d from route %s approaching intersection, turning %s.\n", 
			vtype, vnumber, route, turn_direction);

}

void message_enter(const char* vtype, int vnumber, const char* route, const char* intersection){
	kprintf("%s %d from route %s entering intersection %s.\n", 
			vtype, vnumber, route, intersection);

}

void message_leave(const char* vtype, int vnumber, const char* route, const char* intersection){
	kprintf("%s %d from route %s left intersection %s.\n", 
			vtype, vnumber, route, intersection);


}


/*
 * turnleft()
 *
 * Arguments:
 *      unsigned long vehicledirection: the direction from which the vehicle
 *              approaches the intersection.
 *      unsigned long vehiclenumber: the vehicle id number for printing purposes.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      This function should implement making a left turn through the 
 *      intersection from any direction.
 *      Write and comment this function.
 */

static
void
turnleft(unsigned long vehicledirection,
		unsigned long vehiclenumber,
		unsigned long vehicletype)
{
	/*
	 * Avoid unused variable warnings.
	 */

	(void) vehicledirection;
	(void) vehiclenumber;
	(void) vehicletype;

	//destination lane
	//int dest;

	if(vehicledirection == 0){
		if(vehicletype == 0){
			carsA++;
		}
		if(carsA > 0){
			lock_acquire(tlA);
		}

		//dest = 2;

		//for trucks
		//if(vehicletype = 1){
		
		//enter AB
		lock_acquire(lockAB);
		carsA--;

		if(carsA == 0){
			lock_release(tlA);
		}

		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);
		
		//check if can enter next
		lock_acquire(lockBC);
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);

		lock_release(lockAB);

		//enter next
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);

		//exit 
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);
		lock_release(lockBC);
	}

	//lane B
	else if(vehicledirection == 1){
		if(vehicletype == 0){
			carsB++;
		}
		if(carsB > 0){
			lock_acquire(tlB);
		}

		//dest = 0;

		//for trucks
		//if(vehicletype = 1){
		
		//enter BC
		lock_acquire(lockBC);
		carsB--;
		if(carsB == 0){
			lock_release(tlB);
		}
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);
		
		//check if can enter next
		lock_acquire(lockCA);
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);

		lock_release(lockBC);

		//enter next
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);

		//exit 
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);
		lock_release(lockCA);
	}

	//lane C
	else if(vehicledirection == 2){
		if(vehicletype == 0){
			carsC++;
		}
		if(carsC > 0){
			lock_acquire(tlC);
		}

		//dest = 1;

		//for trucks
		//if(vehicletype = 1){
		
		//enter CA
		lock_acquire(lockCA);
		carsC--;
		if(carsC == 0){
			lock_release(tlC);
		}
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);
		
		//check if can enter next
		lock_acquire(lockAB);
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);

		lock_release(lockCA);

		//enter next
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);

		//exit 
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);
		lock_release(lockAB);
	}
}


/*
 * turnright()
 *
 * Arguments:
 *      unsigned long vehicledirection: the direction from which the vehicle
 *              approaches the intersection.
 *      unsigned long vehiclenumber: the vehicle id number for printing purposes.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      This function should implement making a right turn through the 
 *      intersection from any direction.
 *      Write and comment this function.
 */

static
void
turnright(unsigned long vehicledirection,
		unsigned long vehiclenumber,
		unsigned long vehicletype)
{
	/*
	 * Avoid unused variable warnings.
	 */

	(void) vehicledirection;
	(void) vehiclenumber;
	(void) vehicletype;

	if(vehicledirection == 0){
		if(vehicletype == 0){
			carsA++;
		}
		if(carsA > 0){
			lock_acquire(tlA);
		}

		//dest = 2;

		//for trucks
		//if(vehicletype = 1){
		
		//enter AB
		lock_acquire(lockAB);
		carsA--;

		if(carsA == 0){
			lock_release(tlA);
		}

		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);
		
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[0]);

		lock_release(lockAB);

	}

	//lane B
	else if(vehicledirection == 1){
		if(vehicletype == 0){
			carsB++;
		}
		if(carsB > 0){
			lock_acquire(tlB);
		}

		//dest = 2;

		//for trucks
		//if(vehicletype = 1){
		
		//enter BC
		lock_acquire(lockBC);
		carsB--;
		if(carsB == 0){
			lock_release(tlB);
		}
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);
		
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[1]);

		lock_release(lockBC);
	}

	//lane C
	else if(vehicledirection == 2){
		if(vehicletype == 0){
			carsC++;
		}
		if(carsC > 0){
			lock_acquire(tlC);
		}

		//dest = 0;

		//for trucks
		//if(vehicletype = 1){
		
		//enter CA
		lock_acquire(lockCA);
		carsC--;
		if(carsC == 0){
			lock_release(tlC);
		}
		message_enter(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);
		
		message_leave(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], intersection[2]);

		lock_release(lockCA);
	}

}


/*
 * approachintersection()
 *
 * Arguments: 
 *      void * unusedpointer: currently unused.
 *      unsigned long vehiclenumber: holds vehicle id number.
 *
 * Returns:
 *      nothing.
 *
 * Notes:
 *      Change this function as necessary to implement your solution. These
 *      threads are created by createvehicles().  Each one must choose a direction
 *      randomly, approach the intersection, choose a turn randomly, and then
 *      complete that turn.  The code to choose a direction randomly is
 *      provided, the rest is left to you to implement.  Making a turn
 *      or going straight should be done by calling one of the functions
 *      above.
 */

static
void
approachintersection(void * unusedpointer,
		unsigned long vehiclenumber)
{
	int vehicledirection, turndirection, vehicletype;

	/*
	 * Avoid unused variable and function warnings.
	 */

	(void) unusedpointer;
	(void) vehiclenumber;
	(void) turnleft;
	(void) turnright;

	/*
	 * vehicledirection is set randomly.
	 */
	
	//0 = A, 1 = B, 2 = C
	vehicledirection = random() % 3;
	//
	turndirection = random() % 2;
	//0 = car, 1 = truck
	vehicletype = random() % 2;

	//add approaching intersection message
	message_approach(vehicles[vehicletype], vehiclenumber, 
				routes[vehicledirection], directions[vehicledirection]);

	//make a turn, direction == 0 is left, 1 is right
	if(turndirection == 0){
		turnleft(vehicledirection, vehiclenumber, vehicletype);
	}
	else if(turndirection == 1){
		turnright(vehicledirection, vehiclenumber, vehicletype);
	}

}


/*
 * createvehicles()
 *
 * Arguments:
 *      int nargs: unused.
 *      char ** args: unused.
 *
 * Returns:
 *      0 on success.
 *
 * Notes:
 *      Driver code to start up the approachintersection() threads.  You are
 *      free to modiy this code as necessary for your solution.
 */

int
createvehicles(int nargs,
		char ** args)
{
	int index, error;

	/*
	 * Avoid unused variable warnings.
	 */

	(void) nargs;
	(void) args;

	//create the locks and the temporary lock
	lockAB = lock_create("AB");
	lockBC = lock_create("BC");
	lockCA = lock_create("CA");
	tempy = lock_create("temp");
	
	tlA = lock_create("truckA");
	tlB = lock_create("truckB");
	tlC = lock_create("truckC");
	
	//initially there are no cars in any lane
	carsA = 0;
	carsB = 0;
	carsC = 0;

	/*
	 * Start NVEHICLES approachintersection() threads.
	 */

	for (index = 0; index < NVEHICLES; index++) {

		error = thread_fork("approachintersection thread",
				NULL,
				index,
				approachintersection,
				NULL
				);

		/*
		 * panic() on error.
		 */

		if (error) {

			panic("approachintersection: thread_fork failed: %s\n",
					strerror(error)
				 );
		}
	}

	return 0;
}

