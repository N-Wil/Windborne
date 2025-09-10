#include <types.h>
#include <kern/errno.h>
#include <lib.h>
#include <machine/pcb.h>
#include <machine/spl.h>
#include <machine/trapframe.h>
#include <kern/callno.h>
#include <syscall.h>
#include <addrspace.h>
#include <curthread.h>
#include <thread.h>

/*
 * System call handler.
 *
 * A pointer to the trapframe created during exception entry (in
 * exception.S) is passed in.
 *
 * The calling conventions for syscalls are as follows: Like ordinary
 * function calls, the first 4 32-bit arguments are passed in the 4
 * argument registers a0-a3. In addition, the system call number is
 * passed in the v0 register.
 *
 * On successful return, the return value is passed back in the v0
 * register, like an ordinary function call, and the a3 register is
 * also set to 0 to indicate success.
 *
 * On an error return, the error code is passed back in the v0
 * register, and the a3 register is set to 1 to indicate failure.
 * (Userlevel code takes care of storing the error code in errno and
 * returning the value -1 from the actual userlevel syscall function.
 * See src/lib/libc/syscalls.S and related files.)
 *
 * Upon syscall return the program counter stored in the trapframe
 * must be incremented by one instruction; otherwise the exception
 * return code will restart the "syscall" instruction and the system
 * call will repeat forever.
 *
 * Since none of the OS/161 system calls have more than 4 arguments,
 * there should be no need to fetch additional arguments from the
 * user-level stack.
 *
 * Watch out: if you make system calls that have 64-bit quantities as
 * arguments, they will get passed in pairs of registers, and not
 * necessarily in the way you expect. We recommend you don't do it.
 * (In fact, we recommend you don't use 64-bit quantities at all. See
 * arch/mips/include/types.h.)
 */

void
mips_syscall(struct trapframe *tf)
{
	int callno;
	int32_t retval;
	int err;
	//int for iteration in later for loop
	unsigned int i;

	assert(curspl==0);

	callno = tf->tf_v0;

	/*
	 * Initialize retval to 0. Many of the system calls don't
	 * really return a value, just 0 for success and -1 on
	 * error. Since retval is the value returned on success,
	 * initialize it to 0 by default; thus it's not necessary to
	 * deal with it except for calls that return other values, 
	 * like write.
	 */

	retval = 0;

	switch (callno) {
	    case SYS_reboot:
		err = sys_reboot(tf->tf_a0);
		break;

	    //the patch for the write system call, since I am not implementing it myself this time
	    case SYS_write:
		for(i = 0; i < (size_t) tf->tf_a2; ++i){
			kprintf("%c", ((char*) tf->tf_a1)[i]);
		}
		break;

	    case SYS_getpid:
		retval = sys_getpid() + 1;
		err = 0;
		break;
	    
	    case SYS_getppid:
		retval = sys_getppid() + 1;
		err = 0;		
		break;

	    case SYS_execv:
		err = sys_execv( (char*) tf->tf_a0, (char**) tf->tf_a1);
		break;

	    case SYS_fork:
		retval = sys_fork(tf);
		//retval is now only negative if an error occurred
		if(retval < 0){
			err = -retval;
		}
		else{
			err = 0;
		}

		break;

	    case SYS_waitpid:
		err = sys_waitpid(tf->tf_a0, (userptr_t) tf->tf_a1, tf->tf_a2, &retval);
		break;

	    case SYS__exit:
		sys__exit((int) tf->tf_a0);
		err = 0;
		break;
	
	    /* Add stuff here */
	DEBUG(DB_SYSCALL, "using default"); 
	    default:
		kprintf("Unknown syscall %d\n", callno);
		err = ENOSYS;
		break;
	}


	if (err) {
		/*
		 * Return the error code. This gets converted at
		 * userlevel to a return value of -1 and the error
		 * code in errno.
		 */
		tf->tf_v0 = err;
		tf->tf_a3 = 1;      /* signal an error */
	}
	else {
		/* Success. */
		tf->tf_v0 = retval;
		tf->tf_a3 = 0;      /* signal no error */
	}
	
	/*
	 * Now, advance the program counter, to avoid restarting
	 * the syscall over and over again.
	 */
	
	tf->tf_epc += 4;

	/* Make sure the syscall code didn't forget to lower spl */
	assert(curspl==0);
}

void
md_forkentry(void *data1, unsigned long newpid)
{

	//cast void pointer to real pointer
	unsigned long data[3];
	data[0] = (unsigned long) data1;
	data[1] = (unsigned long) data1 + 1;
	data[2] = (unsigned long) data1 + 2;


	struct trapframe child_tf;
	//copy current trapframe into child trapframe
	memmove(&child_tf, (struct trapframe*) data[0], sizeof(struct trapframe));

	curthread->t_vmspace = (struct addrspace*) data[1];
	curthread->pid = (pid_t) newpid;

	//assign parent pointer
	curthread->parent = (struct thread*) data[2];
	//fix full_process_table[pid] entry that we had saved from previous step in sys_fork
	full_process_table[curthread->pid] = curthread;

		
	//assign members of new tf structure, remember to move program counter
	child_tf.tf_v0 = 0;
	child_tf.tf_a3 = 0;
	child_tf.tf_epc =+ 4;
	
	//release the passed in data
	kfree((struct trapframe*) data[0]);
	kfree(data1);

	as_activate((struct addrspace*) curthread->t_vmspace);

	mips_usermode(&child_tf);
}
