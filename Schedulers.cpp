//Author: Brandon Palomino
//Date: 10/13/17
//Description: Program to simulate the following CPU scheduling algorithms: FCFS, SJF, RoundRobin, Lottery
//			   with the included testdata files

#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
#include <cstdlib>
#include <ctime>
#include <algorithm>
using namespace std;

//global vars for use throughout program
int i=0,j=0,n,bt[30],wt[30],tat[30],avgwt=0,avgtat=0;

void loadFile(ifstream& input, string file);
void FCFS(string file);
void SJF(string file);
void RoundRobin(string file, int quantum);
void Lottery(string file, int quantum);
void clearData();
bool checkData(int data[], int n);

int main(int argc, char const *argv[])
{
	if(argc==2){
		string file = argv[1];
		cout << "Reading file: "<<file<<endl;
		FCFS(file);
		SJF(file);
		RoundRobin(file,25);
		RoundRobin(file,50);
		Lottery(file,60);
	}
	else
		cout<<"Usage: g++ Schedulers.cpp && ./a.out testdata(1-4).txt\n";
}

//First come first serve algorithm
void FCFS(string file){
	cout<<"First Come First Serve:"<<endl;
	ifstream in;

	loadFile(in,file);
	string pid,burstTime;
	while (!in.eof()){
		getline(in,pid);
		getline(in,burstTime);
		bt[i]=stoi(burstTime);
		cout<<"P["<<i+1<<"]:";
		cout<<bt[i]<<endl;
		i++;
		getline(in,pid);		// move stream down one
	}
	in.close();
	n=i;
 
    wt[0]=0;    //waiting time for first process is 0
 
    //calculating waiting time
    for(i=1;i<n;i++){
        wt[i]=0;
        for(j=0;j<i;j++)
            wt[i]+=bt[j]+3; // plus 3 units of CPU TIME
    }
 
    cout<<"\nProcess\t\tBurst Time\tWaiting Time\tTurnaround Time";
 
    //calculating turnaround time
    for(i=0;i<n;i++){
        tat[i]=bt[i]+wt[i];
        avgwt+=wt[i];
        avgtat+=tat[i];
        cout<<"\nP["<<i+1<<"]"<<"\t\t"<<bt[i]<<"\t\t"<<wt[i]<<"\t\t"<<tat[i];
    }
 
    avgwt/=i;
    avgtat/=i;
    cout<<"\n\nAverage Waiting Time:"<<avgwt;
    cout<<"\nAverage Turnaround Time:"<<avgtat<<endl;
    cout<<setfill('-')<<setw(70)<<""<<endl;
    clearData();
}

//shortest job first algorithm
void SJF(string file){
	cout<<"Shortest Job First:"<<endl;
	ifstream in;

	loadFile(in,file);
	string pid,burstTime;
	while (!in.eof()){
		getline(in,pid);
		getline(in,burstTime);
		bt[i]=stoi(burstTime);
		// cout<<"P["<<i+1<<"]:";
		// cout<<bt[i]<<endl;
		i++;
		getline(in,pid);		// move stream down one
	}
	in.close();
	n=i;

	int b[n];
	copy(bt,bt+n,b);

	//sorting processes by order
	sort(b,b+n);
	for(int i=0;i<n;i++){
		cout<<"P["<<i+1<<"]:";
		cout<<b[i]<<endl;
	}
 
    wt[0]=0;    //waiting time for first process is 0
 
    //calculating waiting time
    for(i=1;i<n;i++){
        wt[i]=0;
        for(j=0;j<i;j++)
            wt[i]+=b[j]+3;	// plus 3 units of CPU TIME
    }
 
    cout<<"\nProcess\t\tBurst Time\tWaiting Time\tTurnaround Time";
 
    //calculating turnaround time
    for(i=0;i<n;i++){
        tat[i]=b[i]+wt[i];
        avgwt+=wt[i];
        avgtat+=tat[i];
        cout<<"\nP["<<i+1<<"]"<<"\t\t"<<b[i]<<"\t\t"<<wt[i]<<"\t\t"<<tat[i];
    }
 
    avgwt/=i;
    avgtat/=i;
    cout<<"\n\nAverage Waiting Time:"<<avgwt;
    cout<<"\nAverage Turnaround Time:"<<avgtat<<endl;
    cout<<setfill('-')<<setw(70)<<""<<endl;
    clearData();
}

//round robin algorithm with time quantum input
void RoundRobin(string file, int quantum){
	cout<<"Round Robin with time quantum="<<quantum<<":"<<endl;

	//First getting processes and burst times from file data
	ifstream in;
	loadFile(in,file);
	string pid,burstTime;
	while (!in.eof()){
		getline(in,pid);
		getline(in,burstTime);
		bt[i]=stoi(burstTime);
		cout<<"P["<<i+1<<"]:";
		cout<<bt[i]<<endl;
		i++;
		getline(in,pid);		// move stream down one
	}
	in.close();
	n=i;

    //calculating waiting time
    int rem_bt[n];
    copy(bt,bt+n,rem_bt);	//copy of burst times to store remaining burst times

    int t=0;	// current time
    while(true){							// while processes not done
    	bool done = true;					
    	for(i=0;i<n;i++){					// traverse each process
    		if (rem_bt[i]>0){				
    			done=false;					// there is a pending process
    			if (rem_bt[i] > quantum){	
    				t+=quantum;				// t inc. to show progress on process
    				rem_bt[i]-=quantum;
    			}
    			else{						// last cycle for this process
    				t+=rem_bt[i];			// increating t
    				wt[i]=t-bt[i]+3;		// setting wait time plus 3 units of CPU TIME
    				rem_bt[i]=0;			// fully executed process, set to 0
    			}
    		}
    	}
    	if (done==true)						// all processes completed
    		break;
    }

    cout<<"\nProcess\t\tBurst Time\tWaiting Time\tTurnaround Time";

	//calculating turnaround time
    for(i=0;i<n;i++){
        tat[i]=bt[i]+wt[i];
        avgwt+=wt[i];
        avgtat+=tat[i];
        cout<<"\nP["<<i+1<<"]"<<"\t\t"<<bt[i]<<"\t\t"<<wt[i]<<"\t\t"<<tat[i];
    }
 
    avgwt/=i;
    avgtat/=i;
    cout<<"\n\nAverage Waiting Time:"<<avgwt;
    cout<<"\nAverage Turnaround Time:"<<avgtat<<endl;
    cout<<setfill('-')<<setw(70)<<""<<endl;
    clearData();
}

//lottery algorithm with time quantum input
void Lottery(string file, int quantum){
	cout<<"Lottery with time quantum="<<quantum<<":"<<endl;
	int pty[30];
	//First getting processes and burst times from file data
	ifstream in;
	loadFile(in,file);
	string pid,burstTime,priority;
	while (!in.eof()){
		getline(in,pid);
		getline(in,burstTime);
		getline(in,priority);
		bt[i]=stoi(burstTime);
		pty[i]=stoi(priority);
		cout<<"P["<<i+1<<"]:";
		cout<<bt[i]<<endl;
		i++;
	}
	in.close();
	n=i;

	int ticketTotal = 0;
	for(i=0;i<n;i++)
		ticketTotal+=pty[i];				//calculating total
	srand((int)time(0));					//seeding time
	
	//calculating waiting time
    int rem_bt[n];
    copy(bt,bt+n,rem_bt);	//copy of burst times to store remaining burst times
    int winner=0,t=0;

	while(true){
		winner = rand() % ticketTotal;	//selecting winning ticket
		int current=0;	

		for(i=0;i<n;i++){
			current+=pty[i];
			if (current > winner)
				break;
		}
		if (rem_bt[i]> 0){				
			if (rem_bt[i] > quantum){	
				t+=quantum;				// t inc. to show progress on process
				rem_bt[i]-=quantum;
			}
			else{						// last cycle for this process
				t+=rem_bt[i];			// increating t
				wt[i]=t-bt[i]+3;			// setting wait time  plus 3 units of CPU TIME
				rem_bt[i]=0;			// fully executed process, set to 0
			}
    	}
    	
		if (checkData(rem_bt,n))
			break;
	}

	cout<<"\nProcess\t\tBurst Time\tWaiting Time\tTurnaround Time";

	//calculating turnaround time
    for(i=0;i<n;i++){
        tat[i]=bt[i]+wt[i];
        avgwt+=wt[i];
        avgtat+=tat[i];
        cout<<"\nP["<<i+1<<"]"<<"\t\t"<<bt[i]<<"\t\t"<<wt[i]<<"\t\t"<<tat[i];
    }
 
    avgwt/=i;
    avgtat/=i;
    cout<<"\n\nAverage Waiting Time:"<<avgwt;
    cout<<"\nAverage Turnaround Time:"<<avgtat<<endl;
    cout<<setfill('-')<<setw(70)<<""<<endl;
    clearData();
}

//helper method for checking an array's data with n size
bool checkData(int data[],int n){
	for(int i=0;i<n;i++)
		if (data[i]!=0)
			return false;
	return true;
}

//cleanup method after each algorithms usage
void clearData(){
	n=0,avgwt=0,avgtat=0,i=0,j=0; //cleaning data
	fill(bt,bt+30,0);
	fill(tat,tat+30,0);
	fill(wt,wt+30,0);
}

//loads a test data file
void loadFile(ifstream& input, string file){
	input.open(file);
	if(input.fail()){
		cout<<"Input file failed to load" << endl;
		exit(1);
	}
}